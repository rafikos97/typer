package pl.rafiki.typer.bet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.rafiki.typer.match.Match;
import pl.rafiki.typer.match.MatchRepository;
import pl.rafiki.typer.user.User;
import pl.rafiki.typer.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BetRepositoryTest {
    @Autowired
    private BetRepository underTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MatchRepository matchRepository;
    private User applicationUser;
    private Match match;

    @BeforeEach
    void setUp() {
        applicationUser = new User(
                "Tester",
                "Testowy",
                "testusername25",
                "testmail@gmail.com",
                "password",
                null
        );

        userRepository.save(applicationUser);

        match = new Match(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                0,
                0,
                false
        );

        matchRepository.save(match);
    }

    @Test
    void existsByUserIdAndMatchId() {
        // given
        Bet bet = new Bet(
                1,
                1,
                match,
                applicationUser,
                false,
                0,
                0,
                0
        );

        underTest.save(bet);

        // when
        boolean expected = underTest.existsByUserIdAndMatchId(applicationUser.getId(), match.getId());

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void findAllByUserId() {
        // given
        Long userId = 1L;

        Bet bet = new Bet(
                1,
                1,
                match,
                applicationUser,
                false,
                0,
                0,
                0
        );

        underTest.save(bet);

        // when
        List<Bet> listOfUsersBet = underTest.findAllByUserId(userId);

        // then
        assertThat(listOfUsersBet).asList().contains(bet);
    }

    @Test
    void findAllByMatchId() {
        // given
        Long matchId = 1L;

        Bet bet = new Bet(
                1,
                1,
                match,
                applicationUser,
                false,
                0,
                0,
                0
        );

        underTest.save(bet);

        // when
        List<Bet> listOfUsersBet = underTest.findAllByMatchId(matchId);

        // then
        assertThat(listOfUsersBet).asList().contains(bet);
    }
}
