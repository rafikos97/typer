package pl.rafiki.typer.batchconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import pl.rafiki.typer.match.Match;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.tournament.TournamentRepository;
import pl.rafiki.typer.tournament.exceptions.TournamentDoesNotExistException;

import java.util.Optional;

public class MatchItemProcessor implements ItemProcessor<Match, Match> {
    private final TournamentRepository tournamentRepository;
    private static final Logger log = LoggerFactory.getLogger(MatchItemProcessor.class);

    public MatchItemProcessor(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public Match process(final Match match) {
        log.info("Processing match: " + match);

        String tournamentCode = match.getTournamentCode();
        Optional<Tournament> tournamentOptional = tournamentRepository.findByTournamentCode(tournamentCode);
        if (tournamentOptional.isEmpty()) {
            throw new TournamentDoesNotExistException("Tournament with code: " + tournamentCode + " does not exist!");
        }

        match.setTournament(tournamentOptional.get());

        return match;
    }
}
