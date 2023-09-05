package pl.rafiki.typer.match.exceptions;

public class MatchIsAlreadyFinishedException extends RuntimeException {

    public MatchIsAlreadyFinishedException(String message) {
        super(message);
    }
}
