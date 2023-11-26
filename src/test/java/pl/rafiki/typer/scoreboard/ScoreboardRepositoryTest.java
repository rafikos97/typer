package pl.rafiki.typer.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.rafiki.typer.security.models.Role;
import pl.rafiki.typer.security.repositories.RoleRepository;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.user.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ScoreboardRepositoryTest {

    @Autowired
    private ScoreboardRepository underTest;
    private Tournament tournament;
    private User user;
    @Mock
    private RoleRepository roleRepository;


    @BeforeEach
    void setUp() {
        tournament = new Tournament(
                "testTournament",
                "tst2023"
        );

        Set<Role> roles = new HashSet<>();
        user = new User(
                "Andrew",
                "Tester",
                "username",
                "a.tester@mail.com",
                "password",
                roles
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
        List<Scoreboard> scoreboardList = underTest.findAllByTournamentId(tournament.getId());

        // then
        assertThat(scoreboardList).asList().contains(scoreboard);
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