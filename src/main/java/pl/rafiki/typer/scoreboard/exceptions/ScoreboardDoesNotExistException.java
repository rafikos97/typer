package pl.rafiki.typer.scoreboard.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.ErrorCode;
import pl.rafiki.typer.exceptionhandling.TyperException;

import static pl.rafiki.typer.exceptionhandling.ErrorCode.SCOREBOARD_DOES_NOT_EXIST;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ScoreboardDoesNotExistException extends TyperException {

    public ScoreboardDoesNotExistException(String message) {
        super(message);
    }

    @Override
    protected ErrorCode getErrorCode() {
        return SCOREBOARD_DOES_NOT_EXIST;
    }
}
