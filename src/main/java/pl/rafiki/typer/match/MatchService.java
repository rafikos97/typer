package pl.rafiki.typer.match;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.bet.BetService;
import pl.rafiki.typer.match.exceptions.MatchDoesNotExistException;
import pl.rafiki.typer.match.exceptions.MatchIsAlreadyFinishedException;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.tournament.TournamentRepository;
import pl.rafiki.typer.tournament.exceptions.TournamentDoesNotExistException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final BetService betService;


    @Autowired
    public MatchService(MatchRepository matchRepository, TournamentRepository tournamentRepository, BetService betService) {
        this.matchRepository = matchRepository;
        this.tournamentRepository = tournamentRepository;
        this.betService = betService;
    }

    public List<Match> getMatches() {
        return matchRepository.findAll();
    }

    public Match getMatch(Long matchId) {
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        if (matchOptional.isEmpty()) {
            throw new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!");
        }
        return matchOptional.get();
    }

    public void addNewMatch(Match match) {
        String tournamentCode = match.getTournamentCode();
        Optional<Tournament> tournamentOptional = tournamentRepository.findByTournamentCode(tournamentCode);
        if (tournamentOptional.isEmpty()) {
            throw new TournamentDoesNotExistException("Tournament with code: " + tournamentCode + " does not exist!");
        }

        match.setTournament(tournamentOptional.get());
        matchRepository.save(match);
    }

    public void putUpdateMatch(Long matchId, Match match) {
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        if (matchOptional.isEmpty()) {
            throw new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!");
        }
        Match existingMatch = matchOptional.get();

        String tournamentCodeFromUpdate = match.getTournamentCode();
        if (tournamentCodeFromUpdate != null) {
            Optional<Tournament> tournamentOptional = tournamentRepository.findByTournamentCode(tournamentCodeFromUpdate);
            if (tournamentOptional.isEmpty()) {
                throw new TournamentDoesNotExistException("Tournament with code: " + tournamentCodeFromUpdate + " does not exist!");
            } else if (!Objects.equals(tournamentOptional.get(), existingMatch.getTournament())) {
                existingMatch.setTournament(tournamentOptional.get());
            }
        }

        String firstTeamNameFromUpdate = match.getFirstTeamName();
        if (firstTeamNameFromUpdate != null && firstTeamNameFromUpdate.length() > 0 && !Objects.equals(firstTeamNameFromUpdate, existingMatch.getFirstTeamName())) {
            existingMatch.setFirstTeamName(firstTeamNameFromUpdate);
        }

        String secondTeamNameFromUpdate = match.getSecondTeamName();
        if (secondTeamNameFromUpdate != null && secondTeamNameFromUpdate.length() > 0 && !Objects.equals(secondTeamNameFromUpdate, existingMatch.getSecondTeamName())) {
            existingMatch.setSecondTeamName(secondTeamNameFromUpdate);
        }

        Integer firstTeamScoreFromUpdate = match.getFirstTeamScore();
        if (firstTeamScoreFromUpdate == null) {
            existingMatch.setFirstTeamScore(null);
        } else if (!Objects.equals(firstTeamScoreFromUpdate, existingMatch.getFirstTeamScore())) {
            existingMatch.setFirstTeamScore(firstTeamScoreFromUpdate);
        }

        Integer secondTeamScoreFromUpdate = match.getSecondTeamScore();
        if (secondTeamScoreFromUpdate == null) {
            existingMatch.setSecondTeamScore(null);
        } else if (!Objects.equals(secondTeamScoreFromUpdate, existingMatch.getSecondTeamScore())) {
            existingMatch.setSecondTeamScore(secondTeamScoreFromUpdate);
        }

        LocalDateTime startDateTimeFromUpdate = match.getStartDateAndTime();
        if (!Objects.equals(startDateTimeFromUpdate, existingMatch.getStartDateAndTime())) {
            existingMatch.setStartDateAndTime(startDateTimeFromUpdate);
        }

        matchRepository.save(existingMatch);
    }

    @Transactional
    public void finishMatch(Long matchId) {
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        if (matchOptional.isEmpty()) {
            throw new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!");
        }

        Match existingMatch = matchOptional.get();

        if (existingMatch.isFinished()) {
            throw new MatchIsAlreadyFinishedException("Match is already finished!");
        } else {
            existingMatch.setFinished(true);
        }

        betService.closeBets(matchId);
    }
}
