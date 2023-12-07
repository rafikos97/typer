package pl.rafiki.typer.security.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.ErrorCode;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Getter
@Setter
public class InvalidCredentialsException extends AuthenticationException {
    private final ErrorCode errorCode;

    public InvalidCredentialsException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
