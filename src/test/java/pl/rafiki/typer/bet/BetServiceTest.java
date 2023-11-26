package pl.rafiki.typer.bet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.rafiki.typer.bet.exceptions.BetAlreadyExistException;
import pl.rafiki.typer.bet.exceptions.BetDoesNotExistException;
import pl.rafiki.typer.bet.exceptions.CannotAddBetBecauseMatchAlreadyStartedException;
import pl.rafiki.typer.bet.exceptions.CannotUpdateBetBecauseMatchAlreadyStartedException;
import pl.rafiki.typer.match.Match;
import pl.rafiki.typer.match.MatchRepository;
import pl.rafiki.typer.match.exceptions.MatchDoesNotExistException;
import pl.rafiki.typer.pointrules.PointRules;
import pl.rafiki.typer.scoreboard.ScoreboardService;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.user.User;
import pl.rafiki.typer.user.UserRepository;
import pl.rafiki.typer.user.exceptions.UserDoesNotExistException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BetServiceTest {

    private BetService underTest;
    @Mock
    private BetRepository betRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private ScoreboardService scoreboardService;
    @Mock
    private Match match;
    @Mock
    private User user;

    @BeforeEach
    void setUp() {
        underTest = new BetService(betRepository, userRepository, matchRepository, scoreboardService);
    }

    @Test
    void canAddNewBet() {
        // given
        Long userId = 1L;
        Long matchId = 1L;

        BetDTO bet = new BetDTO(
                userId,
                1,
                1,
                userId
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));
        given(betRepository.existsByUserIdAndMatchId(userId, matchId)).willReturn(false);
        given(match.getStartDateAndTime()).willReturn(LocalDateTime.now().plusDays(1));

        // when
        underTest.addNewBet(userId, matchId, bet);

        // then
        ArgumentCaptor<Bet> betArgumentCaptor = ArgumentCaptor.forClass(Bet.class);
        verify(betRepository).save(betArgumentCaptor.capture());
        Bet capturedBet = betArgumentCaptor.getValue();
        assertThat(capturedBet).isEqualTo(BetMapper.INSTANCE.betDtoToBet(bet, matchRepository, userRepository));
    }

    @Test
    void willThrowWhenUserDoesNotExist() {
        // given
        Long userId = 1L;
        Long matchId = 1L;

        BetDTO bet = new BetDTO(
                userId,
                1,
                1,
                userId
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.of(new Match()));
        given(userRepository.findById(userId)).willReturn(Optional.empty());


        // when
        // then
        assertThatThrownBy(() -> underTest.addNewBet(userId, matchId, bet))
                .isInstanceOf(UserDoesNotExistException.class)
                .hasMessageContaining("User with id: " + userId + " does not exist!");
    }

    @Test
    void willThrowWhenMatchDoesNotExist() {
        // given
        Long userId = 1L;
        Long matchId = 1L;

        BetDTO bet = new BetDTO(
                userId,
                1,
                1,
                userId
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.addNewBet(userId, matchId, bet))
                .isInstanceOf(MatchDoesNotExistException.class)
                .hasMessageContaining("Match with id: " + matchId + " does not exist!");
    }

    @Test
    void willThrowWhenMatchAlreadyStarted() {
        // given
        Long userId = 1L;
        Long matchId = 1L;

        BetDTO bet = new BetDTO(
                userId,
                1,
                1,
                userId
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));
        given(match.getStartDateAndTime()).willReturn(LocalDateTime.now().minusDays(1));

        // when
        // then
        assertThatThrownBy(() -> underTest.addNewBet(userId, matchId, bet))
                .isInstanceOf(CannotAddBetBecauseMatchAlreadyStartedException.class)
                .hasMessageContaining("You cannot place bet, because match already started!");
    }

    @Test
    void willThrowWhenBetAlreadyExist() {
        // given
        Long userId = 1L;
        Long matchId = 1L;

        BetDTO bet = new BetDTO(
                userId,
                1,
                1,
                userId
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));
        given(match.getStartDateAndTime()).willReturn(LocalDateTime.now().plusDays(1));
        given(betRepository.existsByUserIdAndMatchId(userId, matchId)).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.addNewBet(userId, matchId, bet))
                .isInstanceOf(BetAlreadyExistException.class)
                .hasMessageContaining("Bet already exist!");
    }

    @Test
    void canGetAllBets() {
        // when
        underTest.getBets();

        // then
        verify(betRepository, times(1)).findAll();
    }

    @Test
    void canGetUserBets() {
        // given
        Long userId = 1L;

        given(userRepository.existsById(userId)).willReturn(true);

        // when
        underTest.getBetsByUserId(userId);

        // then
        verify(betRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void willThrowWhenUserDoesNotExistWhileGettingBets() {
        // given
        Long userId = 1L;

        given(userRepository.existsById(userId)).willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.getBetsByUserId(userId))
                .isInstanceOf(UserDoesNotExistException.class)
                .hasMessageContaining("User with id: " + userId + " does not exist!");
    }

    @Test
    void canGetBetsByMatchId() {
        // given
        Long matchId = 1L;

        given(matchRepository.existsById(matchId)).willReturn(true);

        // when
        underTest.getBetsByMatchId(matchId);

        // then
        verify(betRepository, times(1)).findAllByMatchId(matchId);
    }

    @Test
    void willThrowWhenMatchDoesNotExistWhileGettingBets() {
        // given
        Long matchId = 1L;

        given(matchRepository.existsById(matchId)).willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.getBetsByMatchId(matchId))
                .isInstanceOf(MatchDoesNotExistException.class)
                .hasMessageContaining("Match with id: " + matchId + " does not exist!");
    }

    @Test
    void canUpdateBet() {
        //given
        Long betId = 1L;

        Bet existingBet = new Bet(
                0,
                0,
                match,
                user,
                false,
                0,
                0,
                0
        );

        BetDTO bet = new BetDTO(
                user.getId(),
                1,
                1,
                match.getId()
        );

        given(betRepository.findById(betId)).willReturn(Optional.of(existingBet));
        given(match.getStartDateAndTime()).willReturn(LocalDateTime.now().plusDays(1));
        given(matchRepository.findById(match.getId())).willReturn(Optional.of(match));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        // when
        underTest.updateBet(betId, bet);

        // then
        ArgumentCaptor<Bet> betArgumentCaptor = ArgumentCaptor.forClass(Bet.class);
        verify(betRepository).save(betArgumentCaptor.capture());
        Bet capturedBet = betArgumentCaptor.getValue();

        assertThat(capturedBet).isEqualTo(BetMapper.INSTANCE.betDtoToBet(bet, matchRepository, userRepository));
    }

    @Test
    void willThrowWhenBetDoesNotExistWhileUpdating() {
        //given
        Long betId = 1L;

        BetDTO bet = new BetDTO(
                null,
                1,
                1,
                null
        );

        given(betRepository.findById(betId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.updateBet(betId, bet))
                .isInstanceOf(BetDoesNotExistException.class)
                .hasMessageContaining("Bet with id: " + betId + " does not exist!");
    }

    @Test
    void willThrowWhenMatchAlreadyStartedWhileUpdating() {
        //given
        Long betId = 1L;

        Bet existingBet = new Bet(
                0,
                0,
                match,
                user,
                false,
                0,
                0,
                0
        );

        BetDTO bet = new BetDTO(
                null,
                1,
                1,
                null
        );

        given(betRepository.findById(betId)).willReturn(Optional.of(existingBet));
        given(match.getStartDateAndTime()).willReturn(LocalDateTime.now().minusDays(1));

        // when
        // then
        assertThatThrownBy(() -> underTest.updateBet(betId, bet))
                .isInstanceOf(CannotUpdateBetBecauseMatchAlreadyStartedException.class)
                .hasMessageContaining("You cannot update bet, because match already started!");
    }

    @Test
    void canCloseBets_DrawWithScore() {
        // given
        Long matchId = 1L;

        PointRules pointRules = new PointRules(
                "pointRulesCode",
                1,
                2
        );

        Tournament tournament = new Tournament(
                "Mistrzostwa Świata 2022",
                "MS2022"
        );

        tournament.setPointRules(pointRules);

        Match match = new Match(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                1,
                1,
                false
        );

        match.setTournament(tournament);

        Bet bet = new Bet(
                1,
                1,
                match,
                user,
                false,
                0,
                0,
                0
        );

        given(betRepository.findAllByMatchId(matchId)).willReturn(List.of(bet));
        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));

        // when
        underTest.closeBets(matchId);

        // then
        verify(scoreboardService, times(1)).addScore(any(), any(), anyInt(), anyInt());
        assertThat(bet.getTotalPoints()).isEqualTo(pointRules.getScore() + pointRules.getWinner());
    }

    @Test
    void canCloseBets_DrawWithoutScore() {
        // given
        Long matchId = 1L;

        PointRules pointRules = new PointRules(
                "pointRulesCode",
                1,
                2
        );

        Tournament tournament = new Tournament(
                "Mistrzostwa Świata 2022",
                "MS2022"
        );

        tournament.setPointRules(pointRules);

        Match match = new Match(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                1,
                1,
                false
        );

        match.setTournament(tournament);

        Bet bet = new Bet(
                2,
                2,
                match,
                user,
                false,
                0,
                0,
                0
        );

        given(betRepository.findAllByMatchId(matchId)).willReturn(List.of(bet));
        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));

        // when
        underTest.closeBets(matchId);

        // then
        verify(scoreboardService, times(1)).addScore(any(), any(), anyInt(), anyInt());
        assertThat(bet.getTotalPoints()).isEqualTo(pointRules.getWinner());
    }

    @Test
    void canCloseBets_WinnerWithScore() {
        // given
        Long matchId = 1L;

        PointRules pointRules = new PointRules(
                "pointRulesCode",
                1,
                2
        );

        Tournament tournament = new Tournament(
                "Mistrzostwa Świata 2022",
                "MS2022"
        );

        tournament.setPointRules(pointRules);

        Match match = new Match(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                2,
                1,
                false
        );

        match.setTournament(tournament);

        Bet bet = new Bet(
                2,
                1,
                match,
                user,
                false,
                0,
                0,
                0
        );

        given(betRepository.findAllByMatchId(matchId)).willReturn(List.of(bet));
        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));

        // when
        underTest.closeBets(matchId);

        // then
        verify(scoreboardService, times(1)).addScore(any(), any(), anyInt(), anyInt());
        assertThat(bet.getTotalPoints()).isEqualTo(pointRules.getWinner() + pointRules.getScore());
    }

    @Test
    void canCloseBets_WinnerWithoutScore() {
        // given
        Long matchId = 1L;

        PointRules pointRules = new PointRules(
                "pointRulesCode",
                1,
                2
        );

        Tournament tournament = new Tournament(
                "Mistrzostwa Świata 2022",
                "MS2022"
        );

        tournament.setPointRules(pointRules);

        Match match = new Match(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                2,
                1,
                false
        );

        match.setTournament(tournament);

        Bet bet = new Bet(
                3,
                1,
                match,
                user,
                false,
                0,
                0,
                0
        );

        given(betRepository.findAllByMatchId(matchId)).willReturn(List.of(bet));
        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));

        // when
        underTest.closeBets(matchId);

        // then
        verify(scoreboardService, times(1)).addScore(any(), any(), anyInt(), anyInt());
        assertThat(bet.getTotalPoints()).isEqualTo(pointRules.getWinner());
    }

    @Test
    void canCloseBets_None() {
        // given
        Long matchId = 1L;

        PointRules pointRules = new PointRules(
                "pointRulesCode",
                1,
                2
        );

        Tournament tournament = new Tournament(
                "Mistrzostwa Świata 2022",
                "MS2022"
        );

        tournament.setPointRules(pointRules);

        Match match = new Match(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                2,
                1,
                false
        );

        match.setTournament(tournament);

        Bet bet = new Bet(
                0,
                0,
                match,
                user,
                false,
                0,
                0,
                0
        );

        given(betRepository.findAllByMatchId(matchId)).willReturn(List.of(bet));
        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));

        // when
        underTest.closeBets(matchId);

        // then
        verify(scoreboardService, times(1)).addScore(any(), any(), anyInt(), anyInt());
        assertThat(bet.getTotalPoints()).isEqualTo(0);
    }

    @Test
    void willThrowWhenMatchDoesNotExistWhileClosingBets() {
        // given
        Long matchId = 1L;

        given(betRepository.findAllByMatchId(matchId)).willReturn(List.of(new Bet()));
        given(matchRepository.findById(matchId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.closeBets(matchId))
                .isInstanceOf(MatchDoesNotExistException.class)
                .hasMessageContaining("Match with id: " + matchId + " does not exist!");
    }
}