package pl.rafiki.typer.match.exceptions;

public class MatchDoesNotExistException extends RuntimeException {

    public MatchDoesNotExistException(String message) {
        super(message);
    }
}
