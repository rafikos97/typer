package pl.rafiki.typer.match;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.bet.BetService;
import pl.rafiki.typer.match.exceptions.MatchCannotBeFinishedDueToNullScoreException;
import pl.rafiki.typer.match.exceptions.MatchDoesNotExistException;
import pl.rafiki.typer.match.exceptions.MatchIsAlreadyFinishedException;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.tournament.TournamentRepository;
import pl.rafiki.typer.tournament.exceptions.TournamentDoesNotExistException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    public List<MatchDTO> getMatches() {
        List<Match> matchList = matchRepository.findAll();

        return matchList
                .stream()
                .map(MatchMapper.INSTANCE::matchToMatchDto)
                .toList();
    }

    public MatchDTO getMatch(Long matchId) {
        Match match = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));

        return MatchMapper.INSTANCE.matchToMatchDto(match);
    }

    public void addNewMatch(MatchDTO matchDTO) {
        Match match = MatchMapper.INSTANCE.matchDtoToMatch(matchDTO, tournamentRepository);
        String tournamentCode = matchDTO.getTournamentCode();

        Tournament tournament = tournamentRepository
                .findByTournamentCode(tournamentCode)
                .orElseThrow(() -> new TournamentDoesNotExistException("Tournament with code: " + tournamentCode + " does not exist!"));

        match.setTournament(tournament);
        matchRepository.save(match);
    }

    public MatchDTO putUpdateMatch(Long matchId, MatchDTO matchDTO) {
        return performMatchUpdate(matchId, matchDTO, true);
    }

    public MatchDTO patchUpdateMatch(Long matchId, MatchDTO matchDTO) {
        return performMatchUpdate(matchId, matchDTO, false);
    }

    private MatchDTO performMatchUpdate(Long matchId, MatchDTO matchDTO, boolean putUpdate) {
        Match existingMatch = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));

        if (existingMatch.isFinished()) {
            throw new MatchIsAlreadyFinishedException("You cannot update match as it's already finished!");
        }

        String tournamentCodeFromUpdate = matchDTO.getTournamentCode();

        if (tournamentCodeFromUpdate != null) {
            Tournament tournament = tournamentRepository
                    .findByTournamentCode(tournamentCodeFromUpdate)
                    .orElseThrow(() -> new TournamentDoesNotExistException("Tournament with code: " + tournamentCodeFromUpdate + " does not exist!"));

            if (!Objects.equals(tournament, existingMatch.getTournament())) {
                existingMatch.setTournament(tournament);
            }
        }

        String firstTeamNameFromUpdate = matchDTO.getFirstTeamName();
        if (firstTeamNameFromUpdate != null && !firstTeamNameFromUpdate.isEmpty() && !Objects.equals(firstTeamNameFromUpdate, existingMatch.getFirstTeamName())) {
            existingMatch.setFirstTeamName(firstTeamNameFromUpdate);
        }

        String secondTeamNameFromUpdate = matchDTO.getSecondTeamName();
        if (secondTeamNameFromUpdate != null && !secondTeamNameFromUpdate.isEmpty() && !Objects.equals(secondTeamNameFromUpdate, existingMatch.getSecondTeamName())) {
            existingMatch.setSecondTeamName(secondTeamNameFromUpdate);
        }

        Integer firstTeamScoreFromUpdate = matchDTO.getFirstTeamScore();
        if (firstTeamScoreFromUpdate == null && putUpdate) {
            existingMatch.setFirstTeamScore(null);
        } else if (!Objects.equals(firstTeamScoreFromUpdate, existingMatch.getFirstTeamScore())) {
            existingMatch.setFirstTeamScore(firstTeamScoreFromUpdate);
        }

        Integer secondTeamScoreFromUpdate = matchDTO.getSecondTeamScore();
        if (secondTeamScoreFromUpdate == null && putUpdate) {
            existingMatch.setSecondTeamScore(null);
        } else if (!Objects.equals(secondTeamScoreFromUpdate, existingMatch.getSecondTeamScore())) {
            existingMatch.setSecondTeamScore(secondTeamScoreFromUpdate);
        }

        LocalDateTime startDateTimeFromUpdate = matchDTO.getStartDateAndTime();
        if (startDateTimeFromUpdate != null && !Objects.equals(startDateTimeFromUpdate, existingMatch.getStartDateAndTime())) {
            existingMatch.setStartDateAndTime(startDateTimeFromUpdate);
        }

        matchRepository.save(existingMatch);
        return MatchMapper.INSTANCE.matchToMatchDto(existingMatch);
    }

    @Transactional
    public void finishMatch(Long matchId) {
        Match existingMatch = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));

        if (existingMatch.getFirstTeamScore() == null || existingMatch.getSecondTeamScore() == null) {
            throw new MatchCannotBeFinishedDueToNullScoreException("Match cannot be finished, because the score is incomplete!");
        }

        if (existingMatch.isFinished()) {
            throw new MatchIsAlreadyFinishedException("Match is already finished!");
        } else {
            existingMatch.setFinished(true);
        }

        betService.closeBets(matchId);
    }
}
