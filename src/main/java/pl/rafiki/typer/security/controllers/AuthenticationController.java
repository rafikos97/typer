package pl.rafiki.typer.security.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.rafiki.typer.security.models.JwtResponse;
import pl.rafiki.typer.security.models.LoginRequest;
import pl.rafiki.typer.security.models.RefreshTokenRequest;
import pl.rafiki.typer.security.services.AuthenticationService;

@RestController
@RequestMapping("typer/auth")
@CrossOrigin("*")
@Tag(name = "Auth", description = "Authentication management APIs")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @Operation(
            summary = "User login.",
            description = "Method that allows user to log in and obtain access token."
    )
    @PostMapping("/login")
    public JwtResponse loginUser(@RequestBody LoginRequest body) {
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }

    @Operation(
            summary = "Access token refresh.",
            description = "Method that allows to refresh access token."
    )
    @PostMapping("/refreshtoken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authenticationService.refreshToken(refreshTokenRequest);
    }
}
