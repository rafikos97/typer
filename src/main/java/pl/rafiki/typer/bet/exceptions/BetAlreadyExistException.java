package pl.rafiki.typer.bet.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.TyperException;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BetAlreadyExistException extends TyperException {

    public BetAlreadyExistException(String message) {
        super(message);
    }

    @Override
    protected String getErrorCode() {
        return "BET_ALREADY_EXISTS";
    }
}
