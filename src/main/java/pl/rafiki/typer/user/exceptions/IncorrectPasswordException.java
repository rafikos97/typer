package pl.rafiki.typer.user.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.TyperException;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class IncorrectPasswordException extends TyperException {

    public IncorrectPasswordException(String message) {
        super(message);
    }

    @Override
    protected String getErrorCode() {
        return "INCORRECT_PASSWORD";
    }
}
