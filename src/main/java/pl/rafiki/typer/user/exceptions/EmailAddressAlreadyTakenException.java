package pl.rafiki.typer.user.exceptions;

public class EmailAddressAlreadyTakenException extends RuntimeException {
    public EmailAddressAlreadyTakenException(String message) {
        super(message);
    }
}
