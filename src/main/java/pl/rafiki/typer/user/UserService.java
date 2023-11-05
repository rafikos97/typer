package pl.rafiki.typer.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.security.exceptions.InvalidCredentialsException;
import pl.rafiki.typer.security.models.Role;
import pl.rafiki.typer.security.repositories.RoleRepository;
import pl.rafiki.typer.user.exceptions.*;

import java.util.*;

import static pl.rafiki.typer.user.validators.EmailValidator.isEmailValid;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public List<UserDTO> getUsers() {
        List<User> userList = userRepository.findAll();

        return userList
                .stream()
                .map(UserMapper.INSTANCE::userToUserDto)
                .toList();
    }

    public UserDTO getUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserDoesNotExistException("User with id: " + userId + " does not exist!");
        }

        User user = userOptional.get();

        return UserMapper.INSTANCE.userToUserDto(user);
    }

    public User registerUser(User user) {
        boolean existsByEmail = userRepository.existsByEmail(user.getEmail());
        if (existsByEmail) {
            throw new EmailAddressAlreadyTakenException("Email address already taken!");
        }

        boolean existsByLogin = userRepository.existsByUsername(user.getUsername());
        if (existsByLogin) {
            throw new UsernameAlreadyTakenException("Login already taken!");
        }

        if (!isEmailValid(user.getEmail())) {
            throw new InvalidEmailException("Provided email is invalid!");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        Role userRole = roleRepository.findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        user.setAuthorities(authorities);
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public void putUpdateUser(Long userId, User user) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserDoesNotExistException("User with id: " + userId + " does not exist!");
        }

        User existingUser = userOptional.get();

        String firstNameFromUpdate = user.getFirstName();
        if (firstNameFromUpdate != null && firstNameFromUpdate.length() > 0 && !Objects.equals(existingUser.getFirstName(), firstNameFromUpdate)) {
            existingUser.setFirstName(firstNameFromUpdate);
        }

        String lastNameFromUpdate = user.getLastName();
        if (lastNameFromUpdate != null && lastNameFromUpdate.length() > 0 && !Objects.equals(existingUser.getLastName(), lastNameFromUpdate)) {
            existingUser.setLastName(lastNameFromUpdate);
        }

        String emailFromUpdate = user.getEmail();
        boolean existsByEmail = userRepository.existsByEmail(emailFromUpdate);

        if (!isEmailValid(emailFromUpdate)) {
            throw new InvalidEmailException("Provided email is invalid!");
        } else if (existsByEmail) {
            throw new EmailAddressAlreadyTakenException("Email address already taken!");
        }

        if (!Objects.equals(existingUser.getEmail(), emailFromUpdate)) {
            existingUser.setEmail(emailFromUpdate);
        }

        String usernameFromUpdate = user.getUsername();
        boolean existsByUsername = userRepository.existsByUsername(usernameFromUpdate);
        if (existsByUsername) {
            throw new UsernameAlreadyTakenException("Username already taken!");
        } else if (usernameFromUpdate != null && usernameFromUpdate.length() > 0 && !Objects.equals(existingUser.getUsername(), usernameFromUpdate)) {
            existingUser.setUsername(usernameFromUpdate);
        }

        userRepository.save(existingUser);
    }

    public void patchUpdateUser(Long userId, UserDTO dto) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserDoesNotExistException("User with id: " + userId + " does not exist!");
        }

        User existingUser = userOptional.get();

        String firstNameFromUpdate = dto.getFirstName();
        if (firstNameFromUpdate != null && firstNameFromUpdate.length() > 0 && !Objects.equals(existingUser.getFirstName(), firstNameFromUpdate)) {
            existingUser.setFirstName(firstNameFromUpdate);
        }

        String lastNameFromUpdate = dto.getLastName();
        if (lastNameFromUpdate != null && lastNameFromUpdate.length() > 0 && !Objects.equals(existingUser.getLastName(), lastNameFromUpdate)) {
            existingUser.setLastName(lastNameFromUpdate);
        }

        String emailFromUpdate = dto.getEmail();
        if (emailFromUpdate != null) {
            boolean existsByEmail = userRepository.existsByEmail(emailFromUpdate);

            if (!isEmailValid(emailFromUpdate)) {
                throw new InvalidEmailException("Provided email is invalid!");
            } else if (existsByEmail) {
                throw new EmailAddressAlreadyTakenException("Email address already taken!");
            }

            if (!Objects.equals(existingUser.getEmail(), emailFromUpdate)) {
                existingUser.setEmail(emailFromUpdate);
            }
        }

        String usernameFromUpdate = dto.getUsername();
        if (usernameFromUpdate != null) {
            boolean existsByUsername = userRepository.existsByUsername(usernameFromUpdate);
            if (existsByUsername) {
                throw new UsernameAlreadyTakenException("Username already taken!");
            } else if (usernameFromUpdate.length() > 0 && !Objects.equals(existingUser.getUsername(), usernameFromUpdate)) {
                existingUser.setUsername(usernameFromUpdate);
            }
        }

        userRepository.save(existingUser);
    }

    @Transactional
    public void updatePassword(Long userId, PasswordDTO dto) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserDoesNotExistException("User with id: " + userId + " does not exist!");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Old password is incorrect!");
        }

        String encodedUpdatedPassword = passwordEncoder.encode(dto.getNewPassword());
        user.setPassword(encodedUpdatedPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("In the user details service");

        Optional<User> userOptional = userRepository.findUserByUsername(username);

        return userOptional.orElseThrow(() -> new InvalidCredentialsException("Invalid credentials!"));
    }
}
