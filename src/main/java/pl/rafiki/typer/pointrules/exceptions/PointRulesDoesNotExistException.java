package pl.rafiki.typer.pointrules.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.ErrorCode;
import pl.rafiki.typer.exceptionhandling.TyperException;

import static pl.rafiki.typer.exceptionhandling.ErrorCode.POINT_RULES_DOES_NOT_EXIST;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PointRulesDoesNotExistException extends TyperException {

    public PointRulesDoesNotExistException(String message) {
        super(message);
    }

    @Override
    protected ErrorCode getErrorCode() {
        return POINT_RULES_DOES_NOT_EXIST;
    }
}
