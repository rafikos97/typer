package pl.rafiki.typer.batchconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import pl.rafiki.typer.match.Match;
import pl.rafiki.typer.match.MatchDTO;
import pl.rafiki.typer.match.MatchMapper;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.tournament.TournamentRepository;
import pl.rafiki.typer.tournament.exceptions.TournamentDoesNotExistException;

public class MatchItemProcessor implements ItemProcessor<MatchDTO, Match> {
    private final TournamentRepository tournamentRepository;
    private static final Logger log = LoggerFactory.getLogger(MatchItemProcessor.class);

    public MatchItemProcessor(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public Match process(final MatchDTO matchDTO) {
        log.info("Processing match: " + matchDTO);

        String tournamentCode = matchDTO.getTournamentCode();
        Match match = MatchMapper.INSTANCE.matchDtoToMatch(matchDTO, tournamentRepository);

        Tournament tournament = tournamentRepository
                .findByTournamentCode(tournamentCode)
                .orElseThrow(() -> new TournamentDoesNotExistException("Tournament with code: " + tournamentCode + " does not exist!"));

        match.setTournament(tournament);

        return match;
    }
}
