package pl.rafiki.typer.security.models;

import lombok.Data;

@Data
public class LoginDTO {
    private String username;
    private String password;
}
