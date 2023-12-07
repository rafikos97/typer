package pl.rafiki.typer.match.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.ErrorCode;
import pl.rafiki.typer.exceptionhandling.TyperException;

import static pl.rafiki.typer.exceptionhandling.ErrorCode.CANNOT_FINISH_MATCH_DUE_TO_NULL_SCORE;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MatchCannotBeFinishedDueToNullScoreException extends TyperException {

    public MatchCannotBeFinishedDueToNullScoreException(String message) {
        super(message);
    }

    @Override
    protected ErrorCode getErrorCode() {
        return CANNOT_FINISH_MATCH_DUE_TO_NULL_SCORE;
    }
}
