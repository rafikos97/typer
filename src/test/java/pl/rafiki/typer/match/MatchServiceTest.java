package pl.rafiki.typer.match;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.rafiki.typer.bet.BetService;
import pl.rafiki.typer.match.exceptions.CannotUpdateTournamentBecauseMatchIsFinishedException;
import pl.rafiki.typer.match.exceptions.MatchDoesNotExistException;
import pl.rafiki.typer.match.exceptions.MatchIsAlreadyFinishedException;
import pl.rafiki.typer.match.exceptions.ScoreCannotBeNullException;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.tournament.TournamentRepository;
import pl.rafiki.typer.tournament.exceptions.TournamentDoesNotExistException;

import java.time.LocalDateTime;
import java.util.List;
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
        underTest.getMatches(null, null, null);

        // then
        verify(matchRepository, times(1)).findAll();
    }

    @Test
    void canGetMatch() {
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

        match.setTournament(tournament);

        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));

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
    void canGetMatchByTournamentId() {
        // given
        Long tournamentId = 1L;

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

        match.setTournament(tournament);

        given(tournamentRepository.existsById(tournamentId)).willReturn(true);
        given(matchRepository.findAllByTournamentId(tournamentId)).willReturn(List.of(match));

        // when
        underTest.getMatchesByTournamentId(tournamentId, null, null, null);

        // then
        verify(matchRepository, times(1)).findAllByTournamentId(tournamentId);
    }

    @Test
    void willThrowWhenTournamentDoesNotExistWhileGettingMatchesByTournamentId() {
        // given
        Long tournamentId = 1L;

        given(tournamentRepository.existsById(tournamentId)).willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.getMatchesByTournamentId(tournamentId, null, null, null))
                .isInstanceOf(TournamentDoesNotExistException.class)
                .hasMessageContaining("Tournament with id: " + tournamentId + " does not exist!");
    }

    @Test
    void canAddNewMatch() {
        // given
        String tournamentCode = "testTournament";
        AddMatchDTO matchDTO = new AddMatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                tournamentCode
        );

        Tournament tournament = new Tournament(
                "test",
                tournamentCode,
                null
        );

        given(tournamentRepository.findByTournamentCode(tournamentCode)).willReturn(Optional.of(tournament));

        // when
        underTest.addNewMatch(matchDTO);

        // then
        ArgumentCaptor<Match> matchArgumentCaptor = ArgumentCaptor.forClass(Match.class);
        verify(matchRepository).save(matchArgumentCaptor.capture());
        Match capturedMatch = matchArgumentCaptor.getValue();
        AddMatchDTO capturedMatchDTO = new AddMatchDTO(capturedMatch.getFirstTeamName(), capturedMatch.getSecondTeamName(), capturedMatch.getStartDateAndTime(), capturedMatch.getTournament().getTournamentCode());
        assertThat(capturedMatchDTO).isEqualTo(matchDTO);
    }

    @Test
    void willThrowWhenTournamentDoesNotExist() {
        // given
        String tournamentCode = "testTournament";
        AddMatchDTO match = new AddMatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
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

        match.setTournament(tournament);

        Tournament tournamentFromUpdate = new Tournament(
                "UpdateTestTournament",
                "UpdateTestTournament"
        );

        UpdateMatchDTO updatedMatch = new UpdateMatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                1,
                1,
                tournamentFromUpdate.getTournamentCode()
        );


        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));
        given(tournamentRepository.findByTournamentCode(tournamentFromUpdate.getTournamentCode())).willReturn(Optional.of(tournamentFromUpdate));

        // when
        underTest.putUpdateMatch(matchId, updatedMatch);

        // then
        ArgumentCaptor<Match> matchArgumentCaptor = ArgumentCaptor.forClass(Match.class);
        verify(matchRepository).save(matchArgumentCaptor.capture());
        Match capturedMatch = matchArgumentCaptor.getValue();
        UpdateMatchDTO capturedMatchDTO = new UpdateMatchDTO(capturedMatch.getFirstTeamName(), capturedMatch.getSecondTeamName(), capturedMatch.getStartDateAndTime(), capturedMatch.getFirstTeamScore(), capturedMatch.getSecondTeamScore(), capturedMatch.getTournament().getTournamentCode());
        assertThat(capturedMatchDTO).isEqualTo(updatedMatch);
    }

    @Test
    void willThrowWhenMatchDoesNotExistDuringUpdate() {
        // given
        Long matchId = 1L;
        String tournamentCode = "testTournament";
        UpdateMatchDTO updatedMatch = new UpdateMatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                0,
                0,
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

        match.setTournament(tournament);

        String tournamentCode = "UpdateTestTournament";
        UpdateMatchDTO updatedMatch = new UpdateMatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                0,
                0,
                tournamentCode
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));
        given(tournamentRepository.findByTournamentCode(tournamentCode)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.putUpdateMatch(matchId, updatedMatch))
                .isInstanceOf(TournamentDoesNotExistException.class)
                .hasMessageContaining("Tournament with code: " + tournamentCode + " does not exist!");
    }

    @Test
    void willThrowWhenMatchIsFinishedAndUserTriesToUpdateTournamentCode() {
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
                true
        );

        match.setTournament(tournament);

        String tournamentCode = "UpdateTestTournament";
        UpdateMatchDTO updatedMatch = new UpdateMatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                0,
                0,
                tournamentCode
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));
        given(tournamentRepository.findByTournamentCode(tournamentCode)).willReturn(Optional.of(new Tournament()));

        // when
        // then
        assertThatThrownBy(() -> underTest.putUpdateMatch(matchId, updatedMatch))
                .isInstanceOf(CannotUpdateTournamentBecauseMatchIsFinishedException.class)
                .hasMessageContaining("Cannot update tournament, because match is already finished!");
    }

    @Test
    void willThrowWhenMatchIsFinishedAndUserTriesToSetFirstTeamScoreToNull() {
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
                true
        );

        match.setTournament(tournament);

        UpdateMatchDTO updatedMatch = new UpdateMatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                null,
                0,
                null
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));

        // when
        // then
        assertThatThrownBy(() -> underTest.putUpdateMatch(matchId, updatedMatch))
                .isInstanceOf(ScoreCannotBeNullException.class)
                .hasMessageContaining("Score cannot be null, because match is already finished!");
    }

    @Test
    void willThrowWhenMatchIsFinishedAndUserTriesToSetSecondTeamScoreToNull() {
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
                true
        );

        match.setTournament(tournament);

        UpdateMatchDTO updatedMatch = new UpdateMatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                0,
                null,
                null
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));

        // when
        // then
        assertThatThrownBy(() -> underTest.putUpdateMatch(matchId, updatedMatch))
                .isInstanceOf(ScoreCannotBeNullException.class)
                .hasMessageContaining("Score cannot be null, because match is already finished!");
    }

    @Test
    void isPointsRecalculationTriggeredWhenFinishedMatchIsUpdated() {
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
                true
        );

        match.setTournament(tournament);

        UpdateMatchDTO updatedMatch = new UpdateMatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                0,
                0,
                null
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));

        // when
        underTest.putUpdateMatch(matchId, updatedMatch);

        // then
        verify(betService, times(1)).recalculatePoints(any(), any());
    }

    @Test
    void areScoreValuesSetToNullWhenTheyAreNotProvidedInPutUpdateRequest() {
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

        match.setTournament(tournament);

        UpdateMatchDTO updatedMatch = new UpdateMatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                null,
                null,
                null
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));

        // when
        underTest.putUpdateMatch(matchId, updatedMatch);

        // then
        ArgumentCaptor<Match> matchArgumentCaptor = ArgumentCaptor.forClass(Match.class);
        verify(matchRepository).save(matchArgumentCaptor.capture());
        Match capturedMatch = matchArgumentCaptor.getValue();
        assertThat(capturedMatch.getFirstTeamScore()).isNull();
        assertThat(capturedMatch.getSecondTeamScore()).isNull();
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

        match.setTournament(tournament);

        Tournament tournamentFromUpdate = new Tournament(
                "UpdateTestTournament",
                "UpdateTestTournament"
        );

        UpdateMatchDTO updatedMatch = new UpdateMatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                1,
                1,
                tournamentFromUpdate.getTournamentCode()
        );


        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));
        given(tournamentRepository.findByTournamentCode(tournamentFromUpdate.getTournamentCode())).willReturn(Optional.of(tournamentFromUpdate));

        // when
        underTest.patchUpdateMatch(matchId, updatedMatch);

        // then
        ArgumentCaptor<Match> matchArgumentCaptor = ArgumentCaptor.forClass(Match.class);
        verify(matchRepository).save(matchArgumentCaptor.capture());
        Match capturedMatch = matchArgumentCaptor.getValue();
        UpdateMatchDTO capturedMatchDTO = new UpdateMatchDTO(capturedMatch.getFirstTeamName(), capturedMatch.getSecondTeamName(), capturedMatch.getStartDateAndTime(), capturedMatch.getFirstTeamScore(), capturedMatch.getSecondTeamScore(), capturedMatch.getTournament().getTournamentCode());
        assertThat(capturedMatchDTO).isEqualTo(updatedMatch);
    }

    @Test
    void doScoreValuesStayUnchangedWhenTheyAreNotProvidedInPatchUpdateRequest() {
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

        match.setTournament(tournament);

        UpdateMatchDTO updatedMatch = new UpdateMatchDTO(
                "Poland",
                "Germany",
                LocalDateTime.now(),
                null,
                null,
                null
        );

        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));

        // when
        underTest.patchUpdateMatch(matchId, updatedMatch);

        // then
        ArgumentCaptor<Match> matchArgumentCaptor = ArgumentCaptor.forClass(Match.class);
        verify(matchRepository).save(matchArgumentCaptor.capture());
        Match capturedMatch = matchArgumentCaptor.getValue();
        assertThat(capturedMatch.getFirstTeamScore()).isEqualTo(match.getFirstTeamScore());
        assertThat(capturedMatch.getSecondTeamScore()).isEqualTo(match.getSecondTeamScore());
    }

    @Test
    void canFinishMatch() {
        // given
        Long matchId = 1L;
        FinishMatchDTO finishMatchDTO = new FinishMatchDTO();

        given(matchRepository.findById(matchId)).willReturn(Optional.of(new Match()));

        // when
        underTest.finishMatch(matchId, finishMatchDTO);

        // then
        verify(betService, times(1)).closeBetsAndCalculatePoints(matchId);
    }

    @Test
    void willThrowWhenMatchDoesNotExistWhenFinishingMatch() {
        // given
        Long matchId = 1L;
        FinishMatchDTO finishMatchDTO = new FinishMatchDTO();

        given(matchRepository.findById(matchId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.finishMatch(matchId, finishMatchDTO))
                .isInstanceOf(MatchDoesNotExistException.class)
                .hasMessageContaining("Match with id: " + matchId + " does not exist!");

        verify(betService, never()).closeBetsAndCalculatePoints(any());
    }

    @Test
    void willThrowWhenMatchIsAlreadyFinished() {
        // given
        Long matchId = 1L;
        FinishMatchDTO finishMatchDTO = new FinishMatchDTO();

        given(matchRepository.findById(matchId)).willReturn(Optional.of(match));
        given(match.isFinished()).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.finishMatch(matchId, finishMatchDTO))
                .isInstanceOf(MatchIsAlreadyFinishedException.class)
                .hasMessageContaining("Match is already finished!");

        verify(betService, never()).closeBetsAndCalculatePoints(any());
    }

    @Test
    void canDeleteMatch() {
        // given
        Long matchId = 1L;

        given(matchRepository.existsById(matchId)).willReturn(true);

        // when
        underTest.deleteMatch(matchId);

        // then
        verify(matchRepository, times(1)).deleteById(any());
    }

    @Test
    void willThrowWhenMatchDoesNotExistWhileDeletingMatch() {
        // given
        Long matchId = 1L;

        given(matchRepository.existsById(matchId)).willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteMatch(matchId))
                .isInstanceOf(MatchDoesNotExistException.class)
                .hasMessageContaining("Match with id: " + matchId + " does not exist!");

        verify(matchRepository, never()).deleteById(any());
    }
}