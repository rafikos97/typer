package pl.rafiki.typer.match;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.rafiki.typer.bet.BetService;
import pl.rafiki.typer.match.exceptions.MatchDoesNotExistException;
import pl.rafiki.typer.match.exceptions.MatchIsAlreadyFinishedException;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.tournament.TournamentRepository;
import pl.rafiki.typer.tournament.exceptions.TournamentDoesNotExistException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    private MatchService underTest;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private BetService betService;
    @Mock
    private Match match;

    @BeforeEach
    void setUp() {
        underTest = new MatchService(matchRepository, tournamentRepository, betService);
    }

    @Test
    void canGetAllMatches() {
        // when
        underTest.getMatches();

        // then
        verify(matchRepository, times(1)).findAll();
    }

    @Test
    void canGetMatch() {
        // given
        Long matchId = 1L;

        given(matchRepository.findById(matchId)).willReturn(Optional.of(new Match()));

        // when
        underTest.getMatch(matchId);

        // then
        verify(matchRepository, times(1)).findById(matchId);
    }

    @Test
    void willThrowWhenMatchDoesNotExist() {
        // given
        Long matchId = 1L;

        given(matchRepository.findById(matchId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getMatch(matchId))
                .isInstanceOf(MatchDoesNotExistException.class)
                .hasMessageContaining("Match with id: " + matchId + " does not exist!");
    }

    @Test
    void canAddNewMatch() {
        // given
        String tournamentCode = "testTournament";
        MatchDTO match = new MatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                0,
                0,
                false,
                tournamentCode
        );

        given(tournamentRepository.findByTournamentCode(tournamentCode)).willReturn(Optional.of(new Tournament()));

        // when
        underTest.addNewMatch(match);

        // then
        ArgumentCaptor<Match> matchArgumentCaptor = ArgumentCaptor.forClass(Match.class);
        verify(matchRepository).save(matchArgumentCaptor.capture());
        Match capturedMatch = matchArgumentCaptor.getValue();
        assertThat(capturedMatch).isEqualTo(match);
    }

    @Test
    void willThrowWhenTournamentDoesNotExist() {
        // given
        String tournamentCode = "testTournament";
        MatchDTO match = new MatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                0,
                0,
                false,
                tournamentCode
        );

        given(tournamentRepository.findByTournamentCode(tournamentCode)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.addNewMatch(match))
                .isInstanceOf(TournamentDoesNotExistException.class)
                .hasMessageContaining("Tournament with code: " + tournamentCode + " does not exist!");
    }

    @Test
    void canUpdateMatch() {
        // given
        Long matchId = 1L;

        Tournament tournament = new Tournament(
                "testTournament",
                "testTournament"
        );

        Match match = new Match(
                "Spain",
                "Italy",
                LocalDateTime.now().minusDays(1),
                2,
                3,
                false
        );

        MatchDTO updatedMatch = new MatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                1,
                1,
                false,
                tournament.getTournamentCode()
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));
        given(tournamentRepository.findByTournamentCode(tournament.getTournamentCode())).willReturn(Optional.of(tournament));

        // when
        underTest.putUpdateMatch(matchId, updatedMatch);

        // then
        ArgumentCaptor<Match> matchArgumentCaptor = ArgumentCaptor.forClass(Match.class);
        verify(matchRepository).save(matchArgumentCaptor.capture());
        Match capturedMatch = matchArgumentCaptor.getValue();
        assertThat(capturedMatch).isEqualTo(updatedMatch);
    }

    @Test
    void willThrowWhenMatchDoesNotExistDuringUpdate() {
        // given
        Long matchId = 1L;
        String tournamentCode = "testTournament";
        MatchDTO updatedMatch = new MatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                0,
                0,
                false,
                tournamentCode
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.putUpdateMatch(matchId, updatedMatch))
                .isInstanceOf(MatchDoesNotExistException.class)
                .hasMessageContaining("Match with id: " + matchId + " does not exist!");
    }

    @Test
    void willThrowWhenTournamentDoesNotExistDuringUpdate() {
        // given
        Long matchId = 1L;
        String tournamentCode = "testTournament";
        MatchDTO updatedMatch = new MatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                0,
                0,
                false,
                tournamentCode
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.of(new Match()));
        given(tournamentRepository.findByTournamentCode(tournamentCode)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.putUpdateMatch(matchId, updatedMatch))
                .isInstanceOf(TournamentDoesNotExistException.class)
                .hasMessageContaining("Tournament with code: " + tournamentCode + " does not exist!");
    }

    @Test
    void canPatchUpdateMatch() {
        // given
        Long matchId = 1L;

        Tournament tournament = new Tournament(
                "testTournament",
                "testTournament"
        );

        Match match = new Match(
                "Spain",
                "Italy",
                LocalDateTime.now().minusDays(1),
                2,
                3,
                false
        );

        MatchDTO updatedMatch = new MatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                1,
                1,
                false,
                tournament.getTournamentCode()
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));
        given(tournamentRepository.findByTournamentCode(tournament.getTournamentCode())).willReturn(Optional.of(tournament));

        // when
        underTest.patchUpdateMatch(matchId, updatedMatch);

        // then
        ArgumentCaptor<Match> matchArgumentCaptor = ArgumentCaptor.forClass(Match.class);
        verify(matchRepository).save(matchArgumentCaptor.capture());
        Match capturedMatch = matchArgumentCaptor.getValue();
        assertThat(capturedMatch.getFirstTeamName()).isEqualTo(updatedMatch.getFirstTeamName());
        assertThat(capturedMatch.getSecondTeamName()).isEqualTo(updatedMatch.getSecondTeamName());
        assertThat(capturedMatch.getStartDateAndTime()).isEqualTo(updatedMatch.getStartDateAndTime());
        assertThat(capturedMatch.getFirstTeamScore()).isEqualTo(updatedMatch.getFirstTeamScore());
        assertThat(capturedMatch.isFinished()).isEqualTo(updatedMatch.isFinished());
        assertThat(capturedMatch.getTournament().getTournamentCode()).isEqualTo(updatedMatch.getTournamentCode());
    }

    @Test
    void willThrowWhenMatchDoesNotExistDuringPatchUpdate() {
        // given
        Long matchId = 1L;
        String tournamentCode = "testTournament";
        MatchDTO updatedMatch = new MatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                1,
                1,
                false,
                tournamentCode
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.patchUpdateMatch(matchId, updatedMatch))
                .isInstanceOf(MatchDoesNotExistException.class)
                .hasMessageContaining("Match with id: " + matchId + " does not exist!");
    }

    @Test
    void willThrowWhenTournamentDoesNotExistDuringPatchUpdate() {
        // given
        Long matchId = 1L;
        String tournamentCode = "testTournament";
        MatchDTO updatedMatch = new MatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                1,
                1,
                false,
                tournamentCode
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.of(new Match()));
        given(tournamentRepository.findByTournamentCode(tournamentCode)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.patchUpdateMatch(matchId, updatedMatch))
                .isInstanceOf(TournamentDoesNotExistException.class)
                .hasMessageContaining("Tournament with code: " + tournamentCode + " does not exist!");
    }

    @Test
    void canFinishMatch() {
        // given
        Long matchId = 1L;

        given(matchRepository.findById(matchId)).willReturn(Optional.of(new Match()));

        // when
        underTest.finishMatch(matchId);

        // then
        verify(betService, times(1)).closeBets(matchId);
    }

    @Test
    void willThrowWhenMatchDoesNotExistWhenFinishingMatch() {
        // given
        Long matchId = 1L;

        given(matchRepository.findById(matchId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.finishMatch(matchId))
                .isInstanceOf(MatchDoesNotExistException.class)
                .hasMessageContaining("Match with id: " + matchId + " does not exist!");

        verify(betService, never()).closeBets(any());
    }

    @Test
    void willThrowWhenMatchIsAlreadyFinished() {
        // given
        Long matchId = 1L;

        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));
        given(match.isFinished()).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.finishMatch(matchId))
                .isInstanceOf(MatchIsAlreadyFinishedException.class)
                .hasMessageContaining("Match is already finished!");

        verify(betService, never()).closeBets(any());
    }
}
