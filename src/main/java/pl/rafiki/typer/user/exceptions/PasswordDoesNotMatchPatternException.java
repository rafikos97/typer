package pl.rafiki.typer.user.exceptions;

public class PasswordDoesNotMatchPatternException extends RuntimeException {

    public PasswordDoesNotMatchPatternException(String message) {
        super(message);
    }
}
