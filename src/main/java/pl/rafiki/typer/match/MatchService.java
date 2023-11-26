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
        String tournamentCode = match.getTournament().getTournamentCode();

        Tournament tournament = tournamentRepository
                .findByTournamentCode(tournamentCode)
                .orElseThrow(() -> new TournamentDoesNotExistException("Tournament with code: " + tournamentCode + " does not exist!"));

        match.setTournament(tournament);
        matchRepository.save(match);
    }

    public MatchDTO putUpdateMatch(Long matchId, MatchDTO matchDTO) {
        Match match = MatchMapper.INSTANCE.matchDtoToMatch(matchDTO, tournamentRepository);
        Match existingMatch = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));

        String tournamentCodeFromUpdate = match.getTournament().getTournamentCode();

        if (tournamentCodeFromUpdate != null) {
            Tournament tournament = tournamentRepository
                    .findByTournamentCode(tournamentCodeFromUpdate)
                    .orElseThrow(() -> new TournamentDoesNotExistException("Tournament with code: " + tournamentCodeFromUpdate + " does not exist!"));

            if (!Objects.equals(tournament, existingMatch.getTournament())) {
                existingMatch.setTournament(tournament);
            }
        }

        String firstTeamNameFromUpdate = match.getFirstTeamName();
        if (firstTeamNameFromUpdate != null && !firstTeamNameFromUpdate.isEmpty() && !Objects.equals(firstTeamNameFromUpdate, existingMatch.getFirstTeamName())) {
            existingMatch.setFirstTeamName(firstTeamNameFromUpdate);
        }

        String secondTeamNameFromUpdate = match.getSecondTeamName();
        if (secondTeamNameFromUpdate != null && !secondTeamNameFromUpdate.isEmpty() && !Objects.equals(secondTeamNameFromUpdate, existingMatch.getSecondTeamName())) {
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
        return MatchMapper.INSTANCE.matchToMatchDto(existingMatch);
    }

    public MatchDTO patchUpdateMatch(Long matchId, MatchDTO dto) {
        Match existingMatch = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));

        String tournamentCodeFromUpdate = dto.getTournamentCode();

        if (tournamentCodeFromUpdate != null) {
            Tournament tournament = tournamentRepository
                    .findByTournamentCode(tournamentCodeFromUpdate)
                    .orElseThrow(() -> new TournamentDoesNotExistException("Tournament with code: " + tournamentCodeFromUpdate + " does not exist!"));

            if (!Objects.equals(tournament, existingMatch.getTournament())) {
                existingMatch.setTournament(tournament);
            }
        }

        String firstTeamNameFromUpdate = dto.getFirstTeamName();
        if (firstTeamNameFromUpdate != null && !firstTeamNameFromUpdate.isEmpty() && !Objects.equals(firstTeamNameFromUpdate, existingMatch.getFirstTeamName())) {
            existingMatch.setFirstTeamName(firstTeamNameFromUpdate);
        }

        String secondTeamNameFromUpdate = dto.getSecondTeamName();
        if (secondTeamNameFromUpdate != null && !secondTeamNameFromUpdate.isEmpty() && !Objects.equals(secondTeamNameFromUpdate, existingMatch.getSecondTeamName())) {
            existingMatch.setSecondTeamName(secondTeamNameFromUpdate);
        }

        Integer firstTeamScoreFromUpdate = dto.getFirstTeamScore();
        if (!Objects.equals(firstTeamScoreFromUpdate, existingMatch.getFirstTeamScore())) {
            existingMatch.setFirstTeamScore(firstTeamScoreFromUpdate);
        }

        Integer secondTeamScoreFromUpdate = dto.getSecondTeamScore();
        if (!Objects.equals(secondTeamScoreFromUpdate, existingMatch.getSecondTeamScore())) {
            existingMatch.setSecondTeamScore(secondTeamScoreFromUpdate);
        }

        LocalDateTime startDateTimeFromUpdate = dto.getStartDateAndTime();
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
