package pl.rafiki.typer.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.user.exceptions.EmailAddressAlreadyTakenException;
import pl.rafiki.typer.user.exceptions.UserDoesNotExistException;
import pl.rafiki.typer.user.exceptions.UsernameAlreadyTakenException;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDTO> userResponseList = new ArrayList<>();
        for (User user : userList) {
            UserDTO userResponse = new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
            userResponseList.add(userResponse);
        }
        return userResponseList;
    }

    public UserDTO getUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserDoesNotExistException("User with id: " + userId + " does not exist!");
        }

        User user = userOptional.get();

        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
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
        if (existsByEmail) {
            throw new EmailAddressAlreadyTakenException("Email address already taken!");
        } else if (emailFromUpdate != null && emailFromUpdate.length() > 0 && !Objects.equals(existingUser.getEmail(), emailFromUpdate)) {
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
}
