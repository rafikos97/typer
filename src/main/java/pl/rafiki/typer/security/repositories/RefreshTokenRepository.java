package pl.rafiki.typer.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import pl.rafiki.typer.security.models.RefreshToken;
import pl.rafiki.typer.user.User;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    boolean existsByUser(User user);

    @Modifying
    int deleteByUser(User user);
}