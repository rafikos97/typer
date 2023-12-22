package pl.rafiki.typer.match;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.bet.BetService;
import pl.rafiki.typer.match.exceptions.CannotUpdateTournamentBecauseMatchIsFinishedException;
import pl.rafiki.typer.match.exceptions.MatchDoesNotExistException;
import pl.rafiki.typer.match.exceptions.MatchIsAlreadyFinishedException;
import pl.rafiki.typer.match.exceptions.ScoreCannotBeNullException;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.tournament.TournamentRepository;
import pl.rafiki.typer.tournament.exceptions.TournamentDoesNotExistException;

import java.time.LocalDate;
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

    public List<MatchDTO> getMatches(Boolean finished, LocalDate startDateFrom, LocalDate startDateTo) {
        return matchRepository
                .findAll()
                .stream()
                .filter(match -> finished == null || match.isFinished() == finished)
                .filter(match -> startDateFrom == null || match.getStartDateAndTime().isAfter(startDateFrom.atStartOfDay()))
                .filter(match -> startDateTo == null || match.getStartDateAndTime().isBefore(startDateTo.atStartOfDay()))
                .map(MatchMapper.INSTANCE::matchToMatchDto)
                .toList();
    }

    public MatchDTO getMatch(Long matchId) {
        Match match = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));

        return MatchMapper.INSTANCE.matchToMatchDto(match);
    }

    public void addNewMatch(AddMatchDTO addMatchDTO) {
        Match match = MatchMapper.INSTANCE.addMatchDtoToMatch(addMatchDTO, tournamentRepository);
        String tournamentCode = addMatchDTO.getTournamentCode();

        Tournament tournament = tournamentRepository
                .findByTournamentCode(tournamentCode)
                .orElseThrow(() -> new TournamentDoesNotExistException("Tournament with code: " + tournamentCode + " does not exist!"));

        match.setTournament(tournament);
        matchRepository.save(match);
    }

    public MatchDTO putUpdateMatch(Long matchId, UpdateMatchDTO updateMatchDTO) {
        return performMatchUpdate(matchId, updateMatchDTO, true);
    }

    public MatchDTO patchUpdateMatch(Long matchId, UpdateMatchDTO updateMatchDTO) {
        return performMatchUpdate(matchId, updateMatchDTO, false);
    }

    private MatchDTO performMatchUpdate(Long matchId, UpdateMatchDTO updateMatchDTO, boolean putUpdate) {
        Match existingMatch = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));

        Match existingMatchCopy = SerializationUtils.clone(existingMatch);

        String tournamentCodeFromUpdate = updateMatchDTO.getTournamentCode();
        if (tournamentCodeFromUpdate != null && !Objects.equals(tournamentCodeFromUpdate, existingMatch.getTournament().getTournamentCode())) {
            Tournament tournament = tournamentRepository
                    .findByTournamentCode(tournamentCodeFromUpdate)
                    .orElseThrow(() -> new TournamentDoesNotExistException("Tournament with code: " + tournamentCodeFromUpdate + " does not exist!"));

            if (existingMatch.isFinished()) {
                throw new CannotUpdateTournamentBecauseMatchIsFinishedException("Cannot update tournament, because match is already finished!");
            }

            existingMatch.setTournament(tournament);
        }

        String firstTeamNameFromUpdate = updateMatchDTO.getFirstTeamName();
        if (firstTeamNameFromUpdate != null && !firstTeamNameFromUpdate.isEmpty() && !Objects.equals(firstTeamNameFromUpdate, existingMatch.getFirstTeamName())) {
            existingMatch.setFirstTeamName(firstTeamNameFromUpdate);
        }

        String secondTeamNameFromUpdate = updateMatchDTO.getSecondTeamName();
        if (secondTeamNameFromUpdate != null && !secondTeamNameFromUpdate.isEmpty() && !Objects.equals(secondTeamNameFromUpdate, existingMatch.getSecondTeamName())) {
            existingMatch.setSecondTeamName(secondTeamNameFromUpdate);
        }

        Integer firstTeamScoreFromUpdate = updateMatchDTO.getFirstTeamScore();
        if (firstTeamScoreFromUpdate == null && existingMatch.isFinished() && putUpdate) {
            throw new ScoreCannotBeNullException("Score cannot be null, because match is already finished!");
        }

        if (firstTeamScoreFromUpdate == null && putUpdate) {
            existingMatch.setFirstTeamScore(null);
        } else if (!Objects.equals(firstTeamScoreFromUpdate, existingMatch.getFirstTeamScore())) {
            existingMatch.setFirstTeamScore(firstTeamScoreFromUpdate);
        }

        Integer secondTeamScoreFromUpdate = updateMatchDTO.getSecondTeamScore();
        if (secondTeamScoreFromUpdate == null && existingMatch.isFinished() && putUpdate) {
            throw new ScoreCannotBeNullException("Score cannot be null, because match is already finished!");
        }

        if (secondTeamScoreFromUpdate == null && putUpdate) {
            existingMatch.setSecondTeamScore(null);
        } else if (!Objects.equals(secondTeamScoreFromUpdate, existingMatch.getSecondTeamScore())) {
            existingMatch.setSecondTeamScore(secondTeamScoreFromUpdate);
        }

        LocalDateTime startDateTimeFromUpdate = updateMatchDTO.getStartDateAndTime();
        if (startDateTimeFromUpdate != null && !Objects.equals(startDateTimeFromUpdate, existingMatch.getStartDateAndTime())) {
            existingMatch.setStartDateAndTime(startDateTimeFromUpdate);
        }

        if (existingMatch.isFinished()) {
            betService.recalculatePoints(existingMatchCopy, existingMatch);
        }

        matchRepository.save(existingMatch);
        return MatchMapper.INSTANCE.matchToMatchDto(existingMatch);
    }

    @Transactional
    public void finishMatch(Long matchId, FinishMatchDTO finishMatchDTO) {
        Match existingMatch = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));

        if (existingMatch.isFinished()) {
            throw new MatchIsAlreadyFinishedException("Match is already finished!");
        } else {
            existingMatch.setFirstTeamScore(finishMatchDTO.getFirstTeamScore());
            existingMatch.setSecondTeamScore(finishMatchDTO.getSecondTeamScore());
            existingMatch.setFinished(true);
        }

        betService.closeBetsAndCalculatePoints(matchId);
    }

    public void deleteMatch(Long matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!");
        }

        matchRepository.deleteById(matchId);
    }
}
