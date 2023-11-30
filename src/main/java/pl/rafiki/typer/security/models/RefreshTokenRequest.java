package pl.rafiki.typer.security.models;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}