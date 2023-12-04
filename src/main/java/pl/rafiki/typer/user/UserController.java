package pl.rafiki.typer.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/typer/profile")
@Validated
@Tag(name = "User", description = "User management APIs")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get all users.",
            description = "Method to get all existing users."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/all")
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @Operation(
            summary = "Get user by userId.",
            description = "Method to get specific user by userId."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/{userId}")
    public UserDTO getUser(@PathVariable("userId") Long userId) {
        return userService.getUser(userId);
    }

    @Operation(
            summary = "Get user by token.",
            description = "Method to fetch user data using token provided in Authorization Header."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping()
    public UserDTO getUser(@RequestHeader("Authorization") String bearerToken) {
        return userService.getUser(bearerToken);
    }

    @Operation(
            summary = "Add new user.",
            description = "Method for adding new user."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/register")
    public void registerUser(@RequestBody @Valid RegisterUserDTO registerUserDTO) {
        userService.registerUser(registerUserDTO);
    }

    @Operation(
            summary = "Update user by userId.",
            description = "Method to update specific user."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping(path = "/{userId}")
    public UserDTO updateUser(@PathVariable("userId") Long userId, @RequestBody @Valid UserDTO userDTO) {
        return userService.putUpdateUser(userId, userDTO);
    }

    @Operation(
            summary = "Update user by token.",
            description = "Method to update user data using token provided in Authorization Header."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping()
    public UserDTO updateUserByToken(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid UserDTO userDTO) {
        return userService.putUpdateUserByToken(bearerToken, userDTO);
    }

    @Operation(
            summary = "Update user by userId.",
            description = "Method to partially update specific user. All fields are optional here."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping(path = "/{userId}")
    public UserDTO patchUpdateUser(@PathVariable("userId") Long userId, @RequestBody UserDTO userDTO) {
        return userService.patchUpdateUser(userId, userDTO);
    }

    @Operation(
            summary = "Change password.",
            description = "Method to change password for specific user."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping(path = "/{userId}/password")
    public void changePassword(@PathVariable("userId") Long userId, @RequestBody PasswordDTO dto) {
        userService.updatePassword(userId, dto);
    }
}
