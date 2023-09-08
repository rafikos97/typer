package pl.rafiki.typer.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "typer/profile/")
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

    @GetMapping(path = "user/{userId}")
    public UserDTO getUser(@PathVariable("userId") Long userId) {
        return userService.getUser(userId);
    }

    @PostMapping(path = "/admin/register")
    public void registerUser(@RequestBody User user) {
        userService.registerUser(user);
    }

    @PutMapping(path = "/user/{userId}")
    public void updateUser(@PathVariable("userId") Long userId, @RequestBody User user) {
        userService.putUpdateUser(userId, user);
    }
}
