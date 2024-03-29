package pl.rafiki.typer.pointrules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.rafiki.typer.pointrules.exceptions.PointRulesCodeAlreadyTakenException;
import pl.rafiki.typer.pointrules.exceptions.PointRulesDoesNotExistException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointRulesServiceTest {

    @Autowired
    private PointRulesService underTest;

    @Mock
    private PointRulesRepository pointRulesRepository;

    @BeforeEach
    void setUp() {
        underTest = new PointRulesService(pointRulesRepository);
    }

    @Test
    void canGetAllPointRules() {
        // when
        underTest.getAllPointRules();

        // then
        verify(pointRulesRepository, times(1)).findAll();
    }

    @Test
    void canGetPointRulesById() {
        // when
        Long pointRulesId = 1L;

        given(pointRulesRepository.findById(pointRulesId)).willReturn(Optional.of(new PointRules()));

        // when
        underTest.getPointRules(pointRulesId);

        // then
        verify(pointRulesRepository, times(1)).findById(pointRulesId);
    }

    @Test
    void willThrowWhenPointRulesDoNotExist() {
        // given
        Long pointRulesId = 1L;

        given(pointRulesRepository.findById(pointRulesId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getPointRules(pointRulesId))
                .isInstanceOf(PointRulesDoesNotExistException.class)
                .hasMessageContaining("Point rules with id: " + pointRulesId + " does not exist!");
    }

    @Test
    void canAddNewPointRules() {
        // given
        String pointrulesCode = "testPointRules";
        PointRulesDTO pointRules = new PointRulesDTO (
                pointrulesCode,
                1,
                1
        );

        given(pointRulesRepository.existsByPointRulesCode(pointrulesCode)).willReturn(false);

        // when
        underTest.addNewPointRules(pointRules);

        // then
        ArgumentCaptor<PointRules> pointRulesArgumentCaptor = ArgumentCaptor.forClass(PointRules.class);
        verify(pointRulesRepository).save(pointRulesArgumentCaptor.capture());
        PointRules capturedPointRules = pointRulesArgumentCaptor.getValue();
        PointRulesDTO capturedPointRulesDTO = new PointRulesDTO(capturedPointRules.getPointRulesCode(), capturedPointRules.getWinner(), capturedPointRules.getScore());
        assertThat(capturedPointRulesDTO).isEqualTo(pointRules);
    }

    @Test
    void willThrowWhenPointRulesCodeAlreadyExists() {
        // given
        String pointrulesCode = "testPointRules";
        PointRulesDTO pointRules = new PointRulesDTO (
                pointrulesCode,
                1,
                1
        );

        given(pointRulesRepository.existsByPointRulesCode(pointrulesCode)).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.addNewPointRules(pointRules))
                .isInstanceOf(PointRulesCodeAlreadyTakenException.class)
                .hasMessageContaining("Point rules code already taken!");
    }

    @Test
    void canUpdatePointRules() {
        // given
        Long pointRulesId = 1L;
        String pointrulesCode = "testPointRules";
        PointRulesDTO updatedPointRules = new PointRulesDTO(
                pointrulesCode,
                1,
                1
        );

        given(pointRulesRepository.findById(pointRulesId)).willReturn(Optional.of(new PointRules()));
        given(pointRulesRepository.existsByPointRulesCode(pointrulesCode)).willReturn(false);

        // when
        underTest.updatePointRules(pointRulesId, updatedPointRules);

        // then
        ArgumentCaptor<PointRules> pointRulesArgumentCaptor = ArgumentCaptor.forClass(PointRules.class);
        verify(pointRulesRepository).save(pointRulesArgumentCaptor.capture());
        PointRules capturedPointRules = pointRulesArgumentCaptor.getValue();
        PointRulesDTO capturedPointRulesDTO = new PointRulesDTO(capturedPointRules.getPointRulesCode(), capturedPointRules.getWinner(), capturedPointRules.getScore());
        assertThat(capturedPointRulesDTO).isEqualTo(updatedPointRules);
    }

    @Test
    void willThrowWhenPointRulesCodeAlreadyExistsDuringUpdate() {
        // given
        Long pointRulesId = 1L;
        String pointrulesCode = "testPointRules";
        PointRulesDTO updatedPointRules = new PointRulesDTO (
                pointrulesCode,
                1,
                1
        );

        given(pointRulesRepository.findById(pointRulesId)).willReturn(Optional.of(new PointRules()));
        given(pointRulesRepository.existsByPointRulesCode(pointrulesCode)).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.updatePointRules(pointRulesId, updatedPointRules))
                .isInstanceOf(PointRulesCodeAlreadyTakenException.class)
                .hasMessageContaining("Point rules code already taken!");

        verify(pointRulesRepository, never()).save(any());
    }

    @Test
    void willThrowWhenPointRulesDoNotExistDuringUpdate() {
        // given
        Long pointRulesId = 1L;
        String pointrulesCode = "testPointRules";
        PointRulesDTO updatedPointRules = new PointRulesDTO (
                pointrulesCode,
                1,
                1
        );

        given(pointRulesRepository.findById(pointRulesId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.updatePointRules(pointRulesId, updatedPointRules))
                .isInstanceOf(PointRulesDoesNotExistException.class)
                .hasMessageContaining("Point rules with id: " + pointRulesId + " does not exist!");

        verify(pointRulesRepository, never()).save(any());
    }

    @Test
    void canDeletePointRules() {
        // given
        Long pointrulesId = 1L;

        given(pointRulesRepository.existsById(pointrulesId)).willReturn(true);

        // when
        underTest.deletePointRules(pointrulesId);

        // then
        verify(pointRulesRepository, times(1)).deleteById(any());
    }

    @Test
    void willThrowWhenPointRulesDoesNotExistWhileDeletingPointRules() {
        // given
        Long pointrulesId = 1L;

        given(pointRulesRepository.existsById(pointrulesId)).willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.deletePointRules(pointrulesId))
                .isInstanceOf(PointRulesDoesNotExistException.class)
                .hasMessageContaining("Point rules with id: " + pointrulesId + " does not exist!");

        verify(pointRulesRepository, never()).deleteById(any());
    }
}
