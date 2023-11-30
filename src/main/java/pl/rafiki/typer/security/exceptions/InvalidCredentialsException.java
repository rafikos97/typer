package pl.rafiki.typer.security.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Getter
@Setter
public class InvalidCredentialsException extends AuthenticationException {
    private final String ERROR_CODE = "INVALID_CREDENTIALS";

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
