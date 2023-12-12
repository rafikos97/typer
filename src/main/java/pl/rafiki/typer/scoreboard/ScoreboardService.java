package pl.rafiki.typer.scoreboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.scoreboard.exceptions.ScoreboardDoesNotExistException;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.tournament.TournamentRepository;
import pl.rafiki.typer.tournament.exceptions.TournamentDoesNotExistException;
import pl.rafiki.typer.user.User;
import pl.rafiki.typer.user.UserRepository;
import pl.rafiki.typer.user.exceptions.UserDoesNotExistException;
import pl.rafiki.typer.utils.Result;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreboardService {

    private final ScoreboardRepository scoreboardRepository;
    private final UserRepository userRepository;
    private final TournamentRepository tournamentRepository;

    @Autowired
    public ScoreboardService(ScoreboardRepository scoreboardRepository, UserRepository userRepository, TournamentRepository tournamentRepository) {
        this.scoreboardRepository = scoreboardRepository;
        this.userRepository = userRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public List<ScoreboardDTO> getScoreboard(Long tournamentId) {
        List<Scoreboard> scoreboardList = scoreboardRepository.findAllByTournamentId(tournamentId);

        return scoreboardList
                .stream()
                .map(ScoreboardMapper.INSTANCE::scoreboardToScoreboardDto)
                .toList();
    }

    public void addScoreToScoreboard(Long userId, Long tournamentId, Result result) {
        int resultForScore = result.getResultForScore();
        int resultForWinner = result.getResultForWinner();

        Optional<Scoreboard> scoreboardOptional = scoreboardRepository.findByUserIdAndTournamentId(userId, tournamentId);
        if (scoreboardOptional.isEmpty()) {
            Scoreboard scoreboardToSave = createScoreboard(userId, tournamentId, resultForScore, resultForWinner);
            scoreboardRepository.save(scoreboardToSave);
        } else {
            Scoreboard existingScoreboard = scoreboardOptional.get();
            Scoreboard scoreboardToSave = updateExistingScoreboard(existingScoreboard, resultForScore, resultForWinner);
            scoreboardRepository.save(scoreboardToSave);
        }
    }

    public void correctScoreInScoreboard(Long userId, Long tournamentId, Result originalResult, Result updatedResult) {
        if (originalResult.equals(updatedResult)) {
            return;
        }

        Scoreboard scoreboard = scoreboardRepository
                .findByUserIdAndTournamentId(userId, tournamentId)
                .orElseThrow(() -> new ScoreboardDoesNotExistException("Cannot correct scoreboard, because it does not exist!"));

        Scoreboard correctedScoreboard = correctExistingScoreboard(scoreboard, originalResult, updatedResult);
        scoreboardRepository.save(correctedScoreboard);
    }

    private Scoreboard createScoreboard(Long userId, Long tournamentId, int resultForScore, int resultForWinner) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User with id: " + userId + " does not exist!"));

        Tournament tournament = tournamentRepository
                .findById(tournamentId)
                .orElseThrow(() -> new TournamentDoesNotExistException("Tournament with id: " + tournamentId + " does not exist!"));

        int totalPoints = resultForScore + resultForWinner;

        return new Scoreboard(user, tournament, resultForWinner, resultForScore, totalPoints);
    }

    private Scoreboard updateExistingScoreboard(Scoreboard existingScoreboard, int resultForScore, int resultForWinner) {
        existingScoreboard.setScores(existingScoreboard.getScores() == null ? resultForScore : existingScoreboard.getScores() + resultForScore);
        existingScoreboard.setWinners(existingScoreboard.getWinners() == null ? resultForWinner : existingScoreboard.getWinners() + resultForWinner);
        existingScoreboard.setTotalPoints(existingScoreboard.getTotalPoints() == null ? resultForScore + resultForWinner : existingScoreboard.getTotalPoints() + resultForScore + resultForWinner);

        return existingScoreboard;
    }

    private Scoreboard correctExistingScoreboard(Scoreboard existingScoreboard, Result originalResult, Result updatedResult) {
        int originalResultForScore = originalResult.getResultForScore();
        int originalResultForWinner = originalResult.getResultForWinner();
        int originalTotalPoints = originalResultForScore + originalResultForWinner;

        existingScoreboard.setScores(existingScoreboard.getScores() - originalResultForScore);
        existingScoreboard.setWinners(existingScoreboard.getWinners() - originalResultForWinner);
        existingScoreboard.setTotalPoints(existingScoreboard.getTotalPoints() - originalTotalPoints);

        return updateExistingScoreboard(existingScoreboard, updatedResult.getResultForScore(), updatedResult.getResultForWinner());
    }
}
