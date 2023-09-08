package pl.rafiki.typer.tournament;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class TournamentRepositoryTest {

    @Autowired
    private TournamentRepository underTest;

    @Test
    void existsByTournamentCode() {
        // given
        String tournamentCode = "MS2022";
        Tournament tournament = new Tournament(
                "Mistrzostwa Świata 2022",
                tournamentCode,
                "pointRulesCode"
        );

        underTest.save(tournament);

        // when
        boolean expected = underTest.existsByTournamentCode(tournamentCode);

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void findByTournamentCode() {
        // given
        String tournamentCode = "MS2022";
        Tournament tournament = new Tournament(
                "Mistrzostwa Świata 2022",
                tournamentCode,
                "pointRulesCode"
        );

        underTest.save(tournament);

        // when
        Optional<Tournament> tournamentOptional = underTest.findByTournamentCode(tournamentCode);

        // then
        assertThat(tournamentOptional).hasValue(tournament);
    }
}