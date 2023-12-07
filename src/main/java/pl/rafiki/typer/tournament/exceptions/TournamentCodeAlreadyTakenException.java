package pl.rafiki.typer.tournament.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.exceptionhandling.ErrorCode;
import pl.rafiki.typer.exceptionhandling.TyperException;

import static pl.rafiki.typer.exceptionhandling.ErrorCode.TOURNAMENT_CODE_ALREADY_TAKEN;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class TournamentCodeAlreadyTakenException extends TyperException {

    public TournamentCodeAlreadyTakenException(String message) {
        super(message);
    }

    @Override
    protected ErrorCode getErrorCode() {
        return TOURNAMENT_CODE_ALREADY_TAKEN;
    }
}
