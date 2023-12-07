package pl.rafiki.typer.user.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.ErrorCode;
import pl.rafiki.typer.exceptionhandling.TyperException;

import static pl.rafiki.typer.exceptionhandling.ErrorCode.USER_DOES_NOT_EXIST;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UserDoesNotExistException extends TyperException {
    public UserDoesNotExistException(String message) {
        super(message);
    }

    @Override
    protected ErrorCode getErrorCode() {
        return USER_DOES_NOT_EXIST;
    }
}
