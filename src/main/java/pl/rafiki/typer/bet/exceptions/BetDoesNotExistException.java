package pl.rafiki.typer.bet.exceptions;

public class BetDoesNotExistException extends RuntimeException {

    public BetDoesNotExistException(String message) {
        super(message);
    }
}
