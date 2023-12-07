package pl.rafiki.typer.bet.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.ErrorCode;
import pl.rafiki.typer.exceptionhandling.TyperException;

import static pl.rafiki.typer.exceptionhandling.ErrorCode.CANNOT_UPDATE_BECAUSE_MATCH_ALREADY_STARTED;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CannotUpdateBetBecauseMatchAlreadyStartedException extends TyperException {

    public CannotUpdateBetBecauseMatchAlreadyStartedException(String message) {
        super(message);
    }

    @Override
    protected ErrorCode getErrorCode() {
        return CANNOT_UPDATE_BECAUSE_MATCH_ALREADY_STARTED;
    }
}
