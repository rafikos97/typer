package pl.rafiki.typer.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.user.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ScoreboardRepositoryTest {

    @Autowired
    private ScoreboardRepository underTest;
    private Tournament tournament;
    private User user;

    @BeforeEach
    void setUp() {
        tournament = new Tournament(
                "testTournament",
                "tst2023",
                false,
                "tstPointRules"
        );

        user = new User(
                "testName",
                "testSurname",
                "testLogin",
                "tstmail@gmail.com"
        );
    }

    @Test
    void findAllByTournamentId() {
        // given
        Scoreboard scoreboard = new Scoreboard(
                user,
                tournament,
                0,
                0,
                0
        );

        underTest.save(scoreboard);

        // when
        List<Scoreboard> scoreList = underTest.findAllByTournamentId(tournament.getId());

        // then
        assertThat(scoreList).asList().contains(scoreboard);
    }

    @Test
    void findByUserIdAndTournamentId() {
        // given
        Scoreboard scoreboard = new Scoreboard(
                user,
                tournament,
                0,
                0,
                0
        );

        underTest.save(scoreboard);

        // when
        Optional<Scoreboard> scoreOptional = underTest.findByUserIdAndTournamentId(user.getId(), tournament.getId());

        // then
        assertThat(scoreOptional).hasValue(scoreboard);
    }
}