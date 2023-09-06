package pl.rafiki.typer.tournament;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.rafiki.typer.pointrules.PointRules;
import pl.rafiki.typer.pointrules.PointRulesRepository;
import pl.rafiki.typer.pointrules.exceptions.PointRulesDoesNotExistException;
import pl.rafiki.typer.tournament.exceptions.TournamentCodeAlreadyTakenException;
import pl.rafiki.typer.tournament.exceptions.TournamentDoesNotExistException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    @Autowired
    private TournamentService underTest;
    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private PointRulesRepository pointRulesRepository;

    @Mock
    private PointRules pointRules;

    @BeforeEach
    void setUp() {
        underTest = new TournamentService(tournamentRepository, pointRulesRepository);
    }

    @Test
    void getTournaments() {
        // when
        underTest.getTournaments();

        // then
        verify(tournamentRepository, times(1)).findAll();
    }

    @Test
    void getTournament() {
        // given
        Long tournamentId = 1L;

        given(tournamentRepository.findById(tournamentId)).willReturn(Optional.of(new Tournament()));

        // when
        underTest.getTournament(tournamentId);

        // then
        verify(tournamentRepository, times(1)).findById(any());
    }

    @Test
    void willThrowWhenTournamentDoesNotExist() {
        // given
        Long tournamentId = 1L;

        given(tournamentRepository.findById(tournamentId)).willReturn(Optional.empty());

        // when
        //then
        assertThatThrownBy(() -> underTest.getTournament(tournamentId))
                .isInstanceOf(TournamentDoesNotExistException.class)
                .hasMessageContaining("Tournament with id: " + tournamentId + " does not exist!");
    }

    @Test
    void addNewTournament() {
        // given
        String pointRulesCode = "testPointRulesCode";
        Tournament tournament = new Tournament(
                "Mistrzostwa Świata 2022",
                "MS2022",
                false,
                pointRulesCode
        );

        given(pointRulesRepository.findPointRulesByPointRulesCode(pointRulesCode)).willReturn(Optional.of(new PointRules()));

        // when
        underTest.addNewTournament(tournament);

        // then
        ArgumentCaptor<Tournament> tournamentArgumentCaptor = ArgumentCaptor.forClass(Tournament.class);
        verify(tournamentRepository).save(tournamentArgumentCaptor.capture());
        Tournament capturedTournament = tournamentArgumentCaptor.getValue();
        assertThat(capturedTournament).isEqualTo(tournament);
    }

    @Test
    void willThrowWhenTournamentCodeIsAlreadyTaken() {
        // given
        String tournamentCode = "MS2022";
        Tournament tournament = new Tournament(
                "Mistrzostwa Świata 2022",
                tournamentCode,
                false,
                "testPointRulesCode"
        );

        given(tournamentRepository.existsByTournamentCode(tournamentCode)).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.addNewTournament(tournament))
                .isInstanceOf(TournamentCodeAlreadyTakenException.class)
                .hasMessageContaining("Tournament code already taken!");
    }

    @Test
    void willThrowWhenPointRulesCodeDoesNotExist() {
        // given
        String pointRulesCode = "testPointRulesCode";
        Tournament tournament = new Tournament(
                "Mistrzostwa Świata 2022",
                "MS2022",
                false,
                pointRulesCode
        );

        given(pointRulesRepository.findPointRulesByPointRulesCode(pointRulesCode)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.addNewTournament(tournament))
                .isInstanceOf(PointRulesDoesNotExistException.class)
                .hasMessageContaining("Point rules with code " + pointRulesCode + " does not exist!");
    }

    @Test
    void updateTournament() {
//        // given

        Long tournamentId = 1L;
        String pointRulesCode = pointRules.getPointRulesCode();
        Tournament updatedTournament = new Tournament(
                "Mistrzostwa Świata 2022",
                "MS2022",
                true,
                pointRulesCode
        );

        updatedTournament.setPointRules(pointRules);

        given(pointRulesRepository.findPointRulesByPointRulesCode(pointRulesCode)).willReturn(Optional.of(pointRules));
        given(tournamentRepository.findById(tournamentId)).willReturn(Optional.of(new Tournament()));

        // when
        underTest.updateTournament(tournamentId, updatedTournament);

        // then
        ArgumentCaptor<Tournament> tournamentArgumentCaptor = ArgumentCaptor.forClass(Tournament.class);
        verify(tournamentRepository).save(tournamentArgumentCaptor.capture());
        Tournament capturedTournament = tournamentArgumentCaptor.getValue();

        assertThat(capturedTournament).isEqualTo(updatedTournament);
    }

    @Test
    void willThrowWhenTournamentDoesNotExistDuringUpdate() {
        // given
        Long tournamentId = 1L;
        String pointRulesCode = "testPointRulesCode";
        Tournament updatedTournament = new Tournament(
                "Mistrzostwa Świata 2022",
                "MS2022",
                false,
                pointRulesCode
        );

        given(tournamentRepository.findById(tournamentId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.updateTournament(tournamentId, updatedTournament))
                .isInstanceOf(TournamentDoesNotExistException.class)
                .hasMessageContaining("Tournament with id: " + tournamentId + " does not exist!");

        verify(tournamentRepository, never()).save(any());
    }

    @Test
    void willThrowWhenTournamentCodeIsAlreadyTakenDuringUpdate() {
        // given
        Long tournamentId = 1L;
        String tournamentCode = "MS2022";
        Tournament updatedTournament = new Tournament(
                "Mistrzostwa Świata 2022",
                tournamentCode,
                false,
                "testPointRulesCode"
        );

        given(tournamentRepository.findById(tournamentId)).willReturn(Optional.of(new Tournament()));
        given(tournamentRepository.existsByTournamentCode(tournamentCode)).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.updateTournament(tournamentId, updatedTournament))
                .isInstanceOf(TournamentCodeAlreadyTakenException.class)
                .hasMessageContaining("Tournament code: " + tournamentCode + " already taken!");

        verify(tournamentRepository, never()).save(any());
    }

    @Test
    void willThrowWhenPointRulesCodeDoesNotExistDuringUpdate() {
        // given
        Long tournamentId = 1L;
        String pointRulesCode = "testPointRulesCode";
        Tournament updatedTournament = new Tournament(
                "Mistrzostwa Świata 2022",
                "MS2022",
                false,
                pointRulesCode
        );

        given(tournamentRepository.findById(tournamentId)).willReturn(Optional.of(new Tournament()));
        given(pointRulesRepository.findPointRulesByPointRulesCode(pointRulesCode)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.updateTournament(tournamentId, updatedTournament))
                .isInstanceOf(PointRulesDoesNotExistException.class)
                .hasMessageContaining("Point rules with code: " + pointRulesCode + " does not exist!");

        verify(tournamentRepository, never()).save(any());
    }
}
