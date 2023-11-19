package pl.rafiki.typer.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String accessToken;
    private String expiresIn;
    private String tokenType;
    private String scope;
}
