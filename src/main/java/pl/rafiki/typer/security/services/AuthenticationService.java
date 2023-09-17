package pl.rafiki.typer.security.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.security.exceptions.InvalidCredentialsException;
import pl.rafiki.typer.security.models.LoginResponseDTO;

import java.time.Duration;
import java.time.Instant;

@Service
@Transactional
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    public ResponseEntity<?> loginUser(String username, String password){

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            Jwt jwt = tokenService.generateJwt(auth);
            String token = jwt.getTokenValue();
            Instant endOfTokenValidity = (Instant) jwt.getClaims().get("exp");
            long expirationTimeInSeconds = Duration.between(Instant.now(), endOfTokenValidity).getSeconds();

            return ResponseEntity.ok(new LoginResponseDTO(token, String.valueOf(expirationTimeInSeconds), "bearer"));
        } catch (AuthenticationException authenticationException) {
            throw new InvalidCredentialsException("Invalid credentials!");
        }
    }
}
