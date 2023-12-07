package pl.rafiki.typer.user.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.ErrorCode;
import pl.rafiki.typer.exceptionhandling.TyperException;

import static pl.rafiki.typer.exceptionhandling.ErrorCode.INVALID_EMAIL;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidEmailException extends TyperException {

    public InvalidEmailException(String message) {
        super(message);
    }

    @Override
    protected ErrorCode getErrorCode() {
        return INVALID_EMAIL;
    }
}
