package pl.rafiki.typer.security.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.security.exceptions.RefreshTokenException;
import pl.rafiki.typer.security.models.RefreshToken;
import pl.rafiki.typer.security.repositories.RefreshTokenRepository;
import pl.rafiki.typer.user.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static pl.rafiki.typer.exceptionhandling.ErrorCode.EXPIRED_REFRESH_TOKEN;

@Service
public class RefreshTokenService {
    @Value("${typer.app.jwtRefreshExpirationSec}")
    private Long refreshTokenValidityTime;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken generateRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenValidityTime));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Refresh token is expired. Please log in again!", EXPIRED_REFRESH_TOKEN);
        }
        return refreshToken;
    }
}