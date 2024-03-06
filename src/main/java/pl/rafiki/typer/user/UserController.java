package pl.rafiki.typer.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> usersList = userService.getUsers();
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    @Operation(
            summary = "Get user by userId.",
            description = "Method to get specific user by userId."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") Long userId) {
        UserDTO user = userService.getUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(
            summary = "Get user by token.",
            description = "Method to fetch user data using token provided in Authorization Header."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping()
    public ResponseEntity<UserDTO> getUser(@RequestHeader("Authorization") String bearerToken) {
        UserDTO user = userService.getUser(bearerToken);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(
            summary = "Add new user.",
            description = "Method for adding new user."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterUserDTO registerUserDTO) {
        userService.registerUser(registerUserDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update user by userId.",
            description = "Method to update specific user."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping(path = "/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("userId") Long userId, @RequestBody @Valid UserDTO userDTO) {
        UserDTO user = userService.putUpdateUser(userId, userDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(
            summary = "Update user by token.",
            description = "Method to update user data using token provided in Authorization Header."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping()
    public ResponseEntity<UserDTO> updateUserByToken(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid UserDTO userDTO) {
        UserDTO user = userService.putUpdateUserByToken(bearerToken, userDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(
            summary = "Update user by userId.",
            description = "Method to partially update specific user. All fields are optional here."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping(path = "/{userId}")
    public ResponseEntity<UserDTO> patchUpdateUser(@PathVariable("userId") Long userId, @RequestBody UserDTO userDTO) {
        UserDTO user = userService.patchUpdateUser(userId, userDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(
            summary = "Change password.",
            description = "Method to change password for specific user."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping(path = "/{userId}/password")
    public ResponseEntity<?> changePassword(@PathVariable("userId") Long userId, @RequestBody PasswordDTO dto) {
        userService.updatePassword(userId, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Delete user.",
            description = "Method to delete specific user by userId."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
