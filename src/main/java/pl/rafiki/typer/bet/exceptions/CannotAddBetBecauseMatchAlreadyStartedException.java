package pl.rafiki.typer.bet.exceptions;

public class CannotAddBetBecauseMatchAlreadyStartedException extends RuntimeException {

    public CannotAddBetBecauseMatchAlreadyStartedException(String message) {
        super(message);
    }
}
