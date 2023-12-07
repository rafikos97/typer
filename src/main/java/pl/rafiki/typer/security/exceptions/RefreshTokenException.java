package pl.rafiki.typer.security.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.ErrorCode;

@ResponseStatus(HttpStatus.FORBIDDEN)
@Getter
@Setter
public class RefreshTokenException extends RuntimeException {
    private final ErrorCode errorCode;

    public RefreshTokenException(String message, ErrorCode errorCode) {
        super(String.format("Failed for: %s", message));
        this.errorCode = errorCode;
    }
}