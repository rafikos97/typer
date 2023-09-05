package pl.rafiki.typer.tournament.exceptions;

public class TournamentDoesNotExistException extends RuntimeException {

    public TournamentDoesNotExistException(String message) {
        super(message);
    }
}
