package pl.rafiki.typer.tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    boolean existsByTournamentCode(String code);
    Optional<Tournament> findByTournamentCode(String code);
}
