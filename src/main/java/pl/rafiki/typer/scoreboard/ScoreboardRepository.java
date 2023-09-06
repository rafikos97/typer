package pl.rafiki.typer.scoreboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreboardRepository extends JpaRepository<Scoreboard, Long> {

    List<Scoreboard> findAllByTournamentId(Long tournamentId);
    Optional<Scoreboard> findByUserIdAndTournamentId(Long userId, Long tournamentId);
}
