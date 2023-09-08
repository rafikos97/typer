package pl.rafiki.typer.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String access_token;
    private String expires_in;
    private String token_type;
}
