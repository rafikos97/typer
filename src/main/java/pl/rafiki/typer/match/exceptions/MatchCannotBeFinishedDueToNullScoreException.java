package pl.rafiki.typer.match.exceptions;

public class MatchCannotBeFinishedDueToNullScoreException extends RuntimeException {

    public MatchCannotBeFinishedDueToNullScoreException(String message) {
        super(message);
    }
}
