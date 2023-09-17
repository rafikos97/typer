package pl.rafiki.typer.security.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.AuthenticationException;

@Getter
@Setter
public class InvalidCredentialsException extends AuthenticationException {
    private final String ERROR_CODE = "INVALID_CREDENTIALS";
    private final HttpStatusCode STATUS_CODE = HttpStatus.INTERNAL_SERVER_ERROR;

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
