package pl.rafiki.typer.security.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<JwtResponse> loginUser(@RequestBody LoginRequest body) {
        JwtResponse jwtResponse = authenticationService.loginUser(body.getUsername(), body.getPassword());
        return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Access token refresh.",
            description = "Method that allows to refresh access token."
    )
    @PostMapping("/refreshtoken")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        JwtResponse jwtResponse = authenticationService.refreshToken(refreshTokenRequest);
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }
}
