package pl.rafiki.typer.tournament.exceptions;

public class TournamentCodeAlreadyTakenException extends RuntimeException {

    public TournamentCodeAlreadyTakenException(String message) {
        super(message);
    }
}
