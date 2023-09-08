package pl.rafiki.typer.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rafiki.typer.security.models.LoginDTO;
import pl.rafiki.typer.security.services.AuthenticationService;

@RestController
@RequestMapping("typer/auth")
@CrossOrigin("*")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO body) {
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }

}
