package pl.rafiki.typer.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.security.models.Role;
import pl.rafiki.typer.security.repositories.RoleRepository;
import pl.rafiki.typer.security.services.TokenService;
import pl.rafiki.typer.user.exceptions.*;
import pl.rafiki.typer.user.validators.PasswordValidator;

import java.util.*;

import static pl.rafiki.typer.user.validators.EmailValidator.isEmailValid;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.tokenService = tokenService;
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

    public UserDTO getUser(String token) {
        String tokenWithoutPrefix = token.substring(7);
        Long userId = tokenService.getUserIdFromToken(tokenWithoutPrefix);
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User with id: " + userId + " does not exist!"));

        return UserMapper.INSTANCE.userToUserDto(user);
    }

    public void registerUser(RegisterUserDTO registerUserDTO) {
        User user = UserMapper.INSTANCE.registerUserDtoToUser(registerUserDTO);

        boolean existsByEmail = userRepository.existsByEmail(registerUserDTO.getEmail());
        if (existsByEmail) {
            throw new EmailAddressAlreadyTakenException("Email address already taken!");
        }

        boolean existsByLogin = userRepository.existsByUsername(registerUserDTO.getUsername());
        if (existsByLogin) {
            throw new UsernameAlreadyTakenException("Login already taken!");
        }

        if (!isEmailValid(registerUserDTO.getEmail())) {
            throw new InvalidEmailException("Provided email is invalid!");
        }

        if (!PasswordValidator.isPasswordValid(registerUserDTO.getPassword())) {
            throw new PasswordDoesNotMatchPatternException("Provided password does not meet password policy!");
        }

        String encodedPassword = passwordEncoder.encode(registerUserDTO.getPassword());
        Role userRole = roleRepository.findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        user.setAuthorities(authorities);
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    public UserDTO putUpdateUser(Long userId, UserDTO userDTO) {
        return performUserUpdate(userId, userDTO);
    }

    public UserDTO putUpdateUserByToken(String token, UserDTO userDTO) {
        String tokenWithoutPrefix = token.substring(7);
        Long userId = tokenService.getUserIdFromToken(tokenWithoutPrefix);

        return performUserUpdate(userId, userDTO);
    }

    public UserDTO patchUpdateUser(Long userId, UserDTO userDTO) {
        return performUserUpdate(userId, userDTO);
    }

    private UserDTO performUserUpdate(Long userId, UserDTO userDTO) {
        User existingUser = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User with id: " + userId + " does not exist!"));

        String firstNameFromUpdate = userDTO.getFirstName();
        if (firstNameFromUpdate != null && !firstNameFromUpdate.isEmpty() && !Objects.equals(existingUser.getFirstName(), firstNameFromUpdate)) {
            existingUser.setFirstName(firstNameFromUpdate);
        }

        String lastNameFromUpdate = userDTO.getLastName();
        if (lastNameFromUpdate != null && !lastNameFromUpdate.isEmpty() && !Objects.equals(existingUser.getLastName(), lastNameFromUpdate)) {
            existingUser.setLastName(lastNameFromUpdate);
        }

        String emailFromUpdate = userDTO.getEmail();
        boolean existsByEmail = userRepository.existsByEmail(emailFromUpdate);

        if (!isEmailValid(emailFromUpdate)) {
            throw new InvalidEmailException("Provided email is invalid!");
        }

        if (!Objects.equals(existingUser.getEmail(), emailFromUpdate)) {
            if (existsByEmail) {
                throw new EmailAddressAlreadyTakenException("Email address already taken!");
            } else {
                existingUser.setEmail(emailFromUpdate);
            }
        }

        String usernameFromUpdate = userDTO.getUsername();
        boolean existsByUsername = userRepository.existsByUsername(usernameFromUpdate);

        if (usernameFromUpdate != null && !usernameFromUpdate.isEmpty() && !Objects.equals(existingUser.getUsername(), usernameFromUpdate)) {
            if (existsByUsername) {
                throw new UsernameAlreadyTakenException("Username already taken!");
            } else {
                existingUser.setUsername(usernameFromUpdate);
            }
        }

        userRepository.save(existingUser);
        return UserMapper.INSTANCE.userToUserDto(existingUser);
    }

    @Transactional
    public void updatePassword(Long userId, PasswordDTO dto) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User with id: " + userId + " does not exist!"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Old password is incorrect!");
        }

        if (!PasswordValidator.isPasswordValid(dto.getNewPassword())) {
            throw new PasswordDoesNotMatchPatternException("Provided password does not meet password policy!");
        }

        String encodedUpdatedPassword = passwordEncoder.encode(dto.getNewPassword());
        user.setPassword(encodedUpdatedPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    public UserDetails loadUserByUserId(Long userId) throws UserDoesNotExistException {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User with id: " + userId + " not found!"));
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserDoesNotExistException("User with id: " + userId + " does not exist!");
        }

        userRepository.deleteById(userId);
    }
}