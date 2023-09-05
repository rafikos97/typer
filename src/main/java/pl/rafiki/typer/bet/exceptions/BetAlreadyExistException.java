package pl.rafiki.typer.bet.exceptions;

public class BetAlreadyExistException extends RuntimeException {

    public BetAlreadyExistException(String message) {
        super(message);
    }
}
