package pl.rafiki.typer.bet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

    boolean existsByUserIdAndMatchId(Long userId, Long matchId);
    List<Bet> findAllByUserId(Long userId);
    List<Bet> findAllByMatchId(Long matchId);
}
