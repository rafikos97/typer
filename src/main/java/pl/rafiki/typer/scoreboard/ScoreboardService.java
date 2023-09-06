package pl.rafiki.typer.scoreboard;

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

    public ScoreboardService(ScoreboardRepository scoreboardRepository, UserRepository userRepository, TournamentRepository tournamentRepository) {
        this.scoreboardRepository = scoreboardRepository;
        this.userRepository = userRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public List<Scoreboard> getScoreboard(Long tournamentId) {
        return scoreboardRepository.findAllByTournamentId(tournamentId);
    }

    public void addScore(Long userId, Long tournamentId, int score, int winner) {
        Optional<Scoreboard> scoreOptional = scoreboardRepository.findByUserIdAndTournamentId(userId, tournamentId);
        if (scoreOptional.isEmpty()) {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                throw new UserDoesNotExistException("User with id: " + userId + " does not exist!");
            }
            User user = userOptional.get();

            Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);
            if (tournamentOptional.isEmpty()) {
                throw new TournamentDoesNotExistException("Tournament with id: " + tournamentId + " does not exist!");
            }
            Tournament tournament = tournamentOptional.get();

            int totalPoints = score + winner;

            Scoreboard scoreboardToSave = new Scoreboard(user, tournament, score, winner, totalPoints);
            scoreboardRepository.save(scoreboardToSave);
        } else {
            Scoreboard existingScoreboard = scoreOptional.get();
            existingScoreboard.setScores(existingScoreboard.getScores() == null ? score : existingScoreboard.getScores() + score);
            existingScoreboard.setWinners(existingScoreboard.getWinners() == null ? winner : existingScoreboard.getWinners() + winner);
            existingScoreboard.setTotalPoints(existingScoreboard.getTotalPoints() == null ? score + winner : existingScoreboard.getTotalPoints() + score + winner);

            scoreboardRepository.save(existingScoreboard);
        }
    }
}
