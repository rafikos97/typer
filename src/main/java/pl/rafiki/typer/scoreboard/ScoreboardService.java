package pl.rafiki.typer.scoreboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.tournament.TournamentRepository;
import pl.rafiki.typer.tournament.exceptions.TournamentDoesNotExistException;
import pl.rafiki.typer.user.User;
import pl.rafiki.typer.user.UserRepository;
import pl.rafiki.typer.user.exceptions.UserDoesNotExistException;

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

    public void addScore(Long userId, Long tournamentId, int score, int winner) {
        Optional<Scoreboard> scoreboardOptional = scoreboardRepository.findByUserIdAndTournamentId(userId, tournamentId);
        if (scoreboardOptional.isEmpty()) {
            User user = userRepository
                    .findById(userId)
                    .orElseThrow(() -> new UserDoesNotExistException("User with id: " + userId + " does not exist!"));

            Tournament tournament = tournamentRepository
                    .findById(tournamentId)
                    .orElseThrow(() -> new TournamentDoesNotExistException("Tournament with id: " + tournamentId + " does not exist!"));

            int totalPoints = score + winner;

            Scoreboard scoreboardToSave = new Scoreboard(user, tournament, winner, score, totalPoints);
            scoreboardRepository.save(scoreboardToSave);
        } else {
            Scoreboard existingScoreboard = scoreboardOptional.get();
            existingScoreboard.setScores(existingScoreboard.getScores() == null ? score : existingScoreboard.getScores() + score);
            existingScoreboard.setWinners(existingScoreboard.getWinners() == null ? winner : existingScoreboard.getWinners() + winner);
            existingScoreboard.setTotalPoints(existingScoreboard.getTotalPoints() == null ? score + winner : existingScoreboard.getTotalPoints() + score + winner);

            scoreboardRepository.save(existingScoreboard);
        }
    }
}
