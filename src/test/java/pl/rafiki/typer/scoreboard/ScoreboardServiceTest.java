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
        int scoreToUpdate = 1;
        int winnerToUpdate = 1;


        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));
        given(tournamentRepository.findById(tournamentId)).willReturn(Optional.of(new Tournament()));

        // when
        underTest.addScore(userId, tournamentId, scoreToUpdate, winnerToUpdate);

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
        int scoreToUpdate = 1;
        int winnerToUpdate = 1;

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.addScore(userId, tournamentId, scoreToUpdate, winnerToUpdate))
                .isInstanceOf(UserDoesNotExistException.class)
                .hasMessageContaining("User with id: " + userId + " does not exist!");
    }

    @Test
    void willThrowWhenTournamentDoesNotExist() {
        // given
        Long userId = 1L;
        Long tournamentId = 1L;
        int scoreToUpdate = 1;
        int winnerToUpdate = 1;

        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));
        given(tournamentRepository.findById(tournamentId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.addScore(userId, tournamentId, scoreToUpdate, winnerToUpdate))
                .isInstanceOf(TournamentDoesNotExistException.class)
                .hasMessageContaining("Tournament with id: " + tournamentId + " does not exist!");
    }

    @Test
    void canAddScoreWhenItAlreadyExists() {
        // given
        Long userId = 1L;
        Long tournamentId = 1L;
        int scoreToUpdate = 1;
        int winnerToUpdate = 1;

        given(scoreRepository.findByUserIdAndTournamentId(userId, tournamentId)).willReturn(Optional.of(new Scoreboard()));

        // when
        underTest.addScore(userId, tournamentId, scoreToUpdate, winnerToUpdate);

        // then
        ArgumentCaptor<Scoreboard> scoreArgumentCaptor = ArgumentCaptor.forClass(Scoreboard.class);
        verify(scoreRepository).save(scoreArgumentCaptor.capture());
        Integer capturedScore = scoreArgumentCaptor.getValue().getScores();
        Integer capturedWinners = scoreArgumentCaptor.getValue().getWinners();
        assertThat(capturedScore).isEqualTo(scoreToUpdate);
        assertThat(capturedWinners).isEqualTo(winnerToUpdate);
    }
}
