package pl.rafiki.typer.match.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.TyperException;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MatchDoesNotExistException extends TyperException {

    public MatchDoesNotExistException(String message) {
        super(message);
    }

    @Override
    protected String getErrorCode() {
        return "MATCH_DOES_NOT_EXIST";
    }
}
