package pl.rafiki.typer.match.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.ErrorCode;
import pl.rafiki.typer.exceptionhandling.TyperException;

import static pl.rafiki.typer.exceptionhandling.ErrorCode.MATCH_IS_ALREADY_FINISHED;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MatchIsAlreadyFinishedException extends TyperException {

    public MatchIsAlreadyFinishedException(String message) {
        super(message);
    }

    @Override
    protected ErrorCode getErrorCode() {
        return MATCH_IS_ALREADY_FINISHED;
    }
}
