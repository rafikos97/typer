package pl.rafiki.typer.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/typer/profile/")
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/admin/all")
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(path = "/user/{userId}")
    public UserDTO getUser(@PathVariable("userId") Long userId) {
        return userService.getUser(userId);
    }

    @PostMapping(path = "/admin/register")
    public void registerUser(@RequestBody @Valid User user) {
        userService.registerUser(user);
    }

    @PutMapping(path = "/user/{userId}")
    public void updateUser(@PathVariable("userId") Long userId, @RequestBody @Valid User user) {
        userService.putUpdateUser(userId, user);
    }

    @PatchMapping(path = "/user/{userId}")
    public void updateUser(@PathVariable("userId") Long userId, @RequestBody UserDTO dto) {
        userService.patchUpdateUser(userId, dto);
    }

    @PatchMapping(path = "/user/{userId}/password")
    public void changePassword(@PathVariable("userId") Long userId, @RequestBody PasswordDTO dto) {
        userService.updatePassword(userId, dto);
    }
}
