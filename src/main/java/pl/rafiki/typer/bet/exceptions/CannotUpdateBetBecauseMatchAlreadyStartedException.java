package pl.rafiki.typer.bet.exceptions;

public class CannotUpdateBetBecauseMatchAlreadyStartedException extends RuntimeException {

    public CannotUpdateBetBecauseMatchAlreadyStartedException(String message) {
        super(message);
    }
}
