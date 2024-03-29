package pl.rafiki.typer.pointrules.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.ErrorCode;
import pl.rafiki.typer.exceptionhandling.TyperException;

import static pl.rafiki.typer.exceptionhandling.ErrorCode.POINT_RULES_CODE_ALREADY_TAKEN;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PointRulesCodeAlreadyTakenException extends TyperException {

    public PointRulesCodeAlreadyTakenException(String message) {
        super(message);
    }

    @Override
    protected ErrorCode getErrorCode() {
        return POINT_RULES_CODE_ALREADY_TAKEN;
    }
}
