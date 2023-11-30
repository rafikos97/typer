package pl.rafiki.typer.security.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
@Getter
@Setter
public class RefreshTokenException extends RuntimeException {
    private final String errorCode;

    public RefreshTokenException(String message, String errorCode) {
        super(String.format("Failed for: %s", message));
        this.errorCode = errorCode;
    }
}