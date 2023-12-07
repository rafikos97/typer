package pl.rafiki.typer.security.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.security.exceptions.InvalidCredentialsException;
import pl.rafiki.typer.security.exceptions.RefreshTokenException;
import pl.rafiki.typer.security.models.JwtResponse;
import pl.rafiki.typer.security.models.RefreshToken;
import pl.rafiki.typer.security.models.RefreshTokenRequest;
import pl.rafiki.typer.security.repositories.RefreshTokenRepository;
import pl.rafiki.typer.user.User;
import pl.rafiki.typer.user.UserService;

import java.time.Duration;
import java.time.Instant;

import static pl.rafiki.typer.exceptionhandling.ErrorCode.INVALID_CREDENTIALS;
import static pl.rafiki.typer.exceptionhandling.ErrorCode.INVALID_REFRESH_TOKEN;

@Service
@Transactional
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public JwtResponse loginUser(String username, String password) {

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            Jwt jwt = tokenService.generateJwt(auth);
            String token = jwt.getTokenValue();
            Instant endOfTokenValidity = (Instant) jwt.getClaims().get("exp");
            long expirationTimeInSeconds = Duration.between(Instant.now(), endOfTokenValidity).getSeconds();
            String scope = jwt.getClaims().get("roles").toString();
            Long userId = tokenService.getUserIdFromToken(jwt.getTokenValue());
            User user = (User) userService.loadUserByUserId(userId);

            if (refreshTokenRepository.existsByUser(user)) {
                refreshTokenRepository.deleteByUser(user);
            }

            String refreshToken = refreshTokenService.generateRefreshToken(userId).getToken();

            return new JwtResponse(token, String.valueOf(expirationTimeInSeconds), "bearer", scope, refreshToken);
        } catch (AuthenticationException authenticationException) {
            throw new InvalidCredentialsException("Invalid credentials!", INVALID_CREDENTIALS);
        }
    }

    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshTokenFromRequest = refreshTokenRequest.getRefreshToken();

        return refreshTokenService.findByToken(refreshTokenFromRequest)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    Jwt jwt = tokenService.generateJwtByUserId(user.getId(), userService);
                    String token = jwt.getTokenValue();
                    Instant endOfTokenValidity = (Instant) jwt.getClaims().get("exp");
                    long expirationTimeInSeconds = Duration.between(Instant.now(), endOfTokenValidity).getSeconds();
                    String scope = jwt.getClaims().get("roles").toString();

                    return new JwtResponse(token, String.valueOf(expirationTimeInSeconds), "bearer", scope, refreshTokenFromRequest);
                })
                .orElseThrow(() -> new RefreshTokenException("Invalid refresh token!", INVALID_REFRESH_TOKEN));
    }
}
