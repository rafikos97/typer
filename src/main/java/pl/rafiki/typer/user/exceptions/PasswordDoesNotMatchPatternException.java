package pl.rafiki.typer.user.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.ErrorCode;
import pl.rafiki.typer.exceptionhandling.TyperException;

import static pl.rafiki.typer.exceptionhandling.ErrorCode.PASSWORD_DOES_NOT_MATCH_PATTERN;

@Getter
@Setter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordDoesNotMatchPatternException extends TyperException {

    public PasswordDoesNotMatchPatternException(String message) {
        super(message);
    }

    @Override
    protected ErrorCode getErrorCode() {
        return PASSWORD_DOES_NOT_MATCH_PATTERN;
    }
}
