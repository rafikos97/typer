package pl.rafiki.typer.bet.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.ErrorCode;
import pl.rafiki.typer.exceptionhandling.TyperException;

import static pl.rafiki.typer.exceptionhandling.ErrorCode.CANNOT_DISPLAY_BETS_AS_MATCH_HAS_NOT_STARTED_YET;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CannotDisplayBetsAsMatchHasNotStartedException extends TyperException {

    public CannotDisplayBetsAsMatchHasNotStartedException(String message) {
        super(message);
    }

    @Override
    protected ErrorCode getErrorCode() {
        return CANNOT_DISPLAY_BETS_AS_MATCH_HAS_NOT_STARTED_YET;
    }
}
