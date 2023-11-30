package pl.rafiki.typer.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String expiresIn;
    private String tokenType;
    private String scope;
    private String refreshToken;
}
