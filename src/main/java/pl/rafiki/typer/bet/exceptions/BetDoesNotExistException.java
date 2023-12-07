package pl.rafiki.typer.bet.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.ErrorCode;
import pl.rafiki.typer.exceptionhandling.TyperException;

import static pl.rafiki.typer.exceptionhandling.ErrorCode.BET_DOES_NOT_EXIST;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BetDoesNotExistException extends TyperException {

    public BetDoesNotExistException(String message) {
        super(message);
    }

    @Override
    protected ErrorCode getErrorCode() {
        return BET_DOES_NOT_EXIST;
    }
}
