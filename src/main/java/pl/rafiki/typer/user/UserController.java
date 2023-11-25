package pl.rafiki.typer.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/typer/profile")
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/all")
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/{userId}")
    public UserDTO getUser(@PathVariable("userId") Long userId) {
        return userService.getUser(userId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping()
    public UserDTO getUser(@RequestHeader("Authorization") String bearerToken) {
        return userService.getUser(bearerToken);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/register")
    public void registerUser(@RequestBody @Valid RegisterUserDTO registerUserDTO) {
        userService.registerUser(registerUserDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping(path = "/{userId}")
    public UserDTO updateUser(@PathVariable("userId") Long userId, @RequestBody @Valid UserDTO userDTO) {
        return userService.putUpdateUser(userId, userDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping()
    public UserDTO updateUserByToken(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid UserDTO userDTO) {
        return userService.putUpdateUserByToken(bearerToken, userDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping(path = "/{userId}")
    public UserDTO patchUpdateUser(@PathVariable("userId") Long userId, @RequestBody UserDTO userDTO) {
        return userService.patchUpdateUser(userId, userDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping(path = "/{userId}/password")
    public void changePassword(@PathVariable("userId") Long userId, @RequestBody PasswordDTO dto) {
        userService.updatePassword(userId, dto);
    }
}
