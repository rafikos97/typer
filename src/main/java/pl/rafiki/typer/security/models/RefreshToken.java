package pl.rafiki.typer.security.models;

import jakarta.persistence.*;
import lombok.Data;
import pl.rafiki.typer.user.User;

import java.time.Instant;

@Entity(name = "refreshToken")
@Table(name = "refresh_tokens")
@Data
public class RefreshToken {
    @Id
    @SequenceGenerator(
            name = "refresh_token_sequence",
            sequenceName = "refresh_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "refresh_token_sequence"
    )
    @Column(
            name = "role_id",
            updatable = false
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(
            name = "token",
            nullable = false,
            unique = true
    )
    private String token;

    @Column(
            nullable = false
    )
    private Instant expiryDate;
}