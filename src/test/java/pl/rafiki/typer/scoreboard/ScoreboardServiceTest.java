package pl.rafiki.typer.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.tournament.TournamentRepository;
import pl.rafiki.typer.tournament.exceptions.TournamentDoesNotExistException;
import pl.rafiki.typer.user.User;
import pl.rafiki.typer.user.UserRepository;
import pl.rafiki.typer.user.exceptions.UserDoesNotExistException;
import pl.rafiki.typer.utils.Result;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScoreboardServiceTest {
    @Autowired
    private ScoreboardService underTest;
    @Mock
    private ScoreboardRepository scoreRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TournamentRepository tournamentRepository;

    @BeforeEach
    void setUp() {
        underTest = new ScoreboardService(scoreRepository, userRepository, tournamentRepository);
    }

    @Test
    void canGetScoreboard() {
        // given
        Long tournamentId = 1L;

        // when
        underTest.getScoreboard(tournamentId);

        // then
        verify(scoreRepository, times(1)).findAllByTournamentId(tournamentId);
    }

    @Test
    void canAddScore() {
        // given
        Long userId = 1L;
        Long tournamentId = 1L;
        Result result = new Result(1, 1);
        int scoreToUpdate = result.getResultForScore();
        int winnerToUpdate = result.getResultForWinner();


        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));
        given(tournamentRepository.findById(tournamentId)).willReturn(Optional.of(new Tournament()));

        // when
        underTest.addScoreToScoreboard(userId, tournamentId, result);

        // then
        ArgumentCaptor<Scoreboard> scoreArgumentCaptor = ArgumentCaptor.forClass(Scoreboard.class);
        verify(scoreRepository).save(scoreArgumentCaptor.capture());
        Integer capturedScore = scoreArgumentCaptor.getValue().getScores();
        Integer capturedWinners = scoreArgumentCaptor.getValue().getWinners();
        assertThat(capturedScore).isEqualTo(scoreToUpdate);
        assertThat(capturedWinners).isEqualTo(winnerToUpdate);
    }

    @Test
    void willThrowWhenUserDoesNotExist() {
        // given
        Long userId = 1L;
        Long tournamentId = 1L;
        Result result = new Result(1, 1);

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.addScoreToScoreboard(userId, tournamentId, result))
                .isInstanceOf(UserDoesNotExistException.class)
                .hasMessageContaining("User with id: " + userId + " does not exist!");
    }

    @Test
    void willThrowWhenTournamentDoesNotExist() {
        // given
        Long userId = 1L;
        Long tournamentId = 1L;
        Result result = new Result(1, 1);

        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));
        given(tournamentRepository.findById(tournamentId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.addScoreToScoreboard(userId, tournamentId, result))
                .isInstanceOf(TournamentDoesNotExistException.class)
                .hasMessageContaining("Tournament with id: " + tournamentId + " does not exist!");
    }

    @Test
    void canAddScoreWhenItAlreadyExists() {
        // given
        Long userId = 1L;
        Long tournamentId = 1L;
        Result result = new Result(1, 1);

        given(scoreRepository.findByUserIdAndTournamentId(userId, tournamentId)).willReturn(Optional.of(new Scoreboard()));

        // when
        underTest.addScoreToScoreboard(userId, tournamentId, result);

        // then
        ArgumentCaptor<Scoreboard> scoreArgumentCaptor = ArgumentCaptor.forClass(Scoreboard.class);
        verify(scoreRepository).save(scoreArgumentCaptor.capture());
        Integer capturedScore = scoreArgumentCaptor.getValue().getScores();
        Integer capturedWinners = scoreArgumentCaptor.getValue().getWinners();
        assertThat(capturedScore).isEqualTo(result.getResultForScore());
        assertThat(capturedWinners).isEqualTo(result.getResultForWinner());
    }

    @Test
    void canCorrectScore() {
        // given
        Long userId = 1L;
        Long tournamentId = 1L;

        Result originalResult = new Result(2, 1);
        int originalScore = originalResult.getResultForScore();
        int originalWinner = originalResult.getResultForWinner();

        Result updatedResult = new Result(1, 2);
        int scoreToUpdate = updatedResult.getResultForScore();
        int winnerToUpdate = updatedResult.getResultForWinner();

        Scoreboard existingScoreboard = new Scoreboard(
                new User(),
                new Tournament(),
                originalWinner,
                originalScore,
                originalScore + originalWinner
        );

        given(scoreRepository.findByUserIdAndTournamentId(userId, tournamentId)).willReturn(Optional.of(existingScoreboard));

        // when
        underTest.correctScoreInScoreboard(userId, tournamentId, originalResult, updatedResult);

        // then
        ArgumentCaptor<Scoreboard> scoreArgumentCaptor = ArgumentCaptor.forClass(Scoreboard.class);
        verify(scoreRepository).save(scoreArgumentCaptor.capture());
        Integer capturedScore = scoreArgumentCaptor.getValue().getScores();
        Integer capturedWinners = scoreArgumentCaptor.getValue().getWinners();
        assertThat(capturedScore).isEqualTo(scoreToUpdate);
        assertThat(capturedWinners).isEqualTo(winnerToUpdate);
    }
}
