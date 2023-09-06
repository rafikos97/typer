package pl.rafiki.typer.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.rafiki.typer.user.exceptions.EmailAddressAlreadyTakenException;
import pl.rafiki.typer.user.exceptions.UserDoesNotExistException;
import pl.rafiki.typer.user.exceptions.UsernameAlreadyTakenException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository);
    }

    @Test
    void canGetAllUsers() {
        // when
        underTest.getUsers();

        // then
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void canGetUserById() {
        // given
        Long userId = 1L;

        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));

        // when
        underTest.getUser(userId);

        // then
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    void willThrowWhenUserDoesNotExist() {
        // given
        Long userId = 1L;

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getUser(userId))
                .isInstanceOf(UserDoesNotExistException.class)
                .hasMessageContaining("User with id: " + userId + " does not exist!");
    }

    @Test
    void canAddNewUser() {
        // given
        User user = new User(
                "Andrew",
                "Tester",
                "login",
                "a.tester@mail.com"
        );

        // when
        underTest.registerUser(user);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        User user = new User(
                "Andrew",
                "Tester",
                "login",
                "a.tester@mail.com"
        );

        given(userRepository.existsByEmail(user.getEmail())).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.registerUser(user))
                .isInstanceOf(EmailAddressAlreadyTakenException.class)
                .hasMessageContaining("Email address already taken!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void willThrowWhenUsernameIsTaken() {
        // given
        User user = new User(
                "Andrew",
                "Tester",
                "login",
                "a.tester@mail.com"
        );

        given(userRepository.existsByUsername(user.getUsername())).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.registerUser(user))
                .isInstanceOf(UsernameAlreadyTakenException.class)
                .hasMessageContaining("Username already taken!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser() {
        // given
        Long userId = 1L;
        User updatedUser = new User(
                "Adam",
                "Nowak",
                "updatedLogin",
                "a.nowak@mail.com"
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));

        // when
        underTest.putUpdateUser(userId, updatedUser);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(updatedUser);
    }

    @Test
    void willThrowWhenUserDoesNotExistDuringUpdate() {
        // given
        Long userId = 1L;
        User updatedUser = new User(
                "Adam",
                "Nowak",
                "updatedLogin",
                "a.nowak@mail.com"
        );

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.putUpdateUser(userId, updatedUser))
                .isInstanceOf(UserDoesNotExistException.class)
                .hasMessageContaining("User with id: " + userId + " does not exist!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void willThrowWhenEmailAddressIsAlreadyTakenDuringUpdate() {
        // given
        Long userId = 1L;
        String email = "a.nowak@mail.com";
        User updatedUser = new User(
                "Adam",
                "Nowak",
                "updatedLogin",
                email
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));
        given(userRepository.existsByEmail(email)).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.putUpdateUser(userId, updatedUser))
                .isInstanceOf(EmailAddressAlreadyTakenException.class)
                .hasMessageContaining("Email address already taken!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void willThrowWhenEmailUsernameIsAlreadyTakenDuringUpdate() {
        // given
        Long userId = 1L;
        String login = "updatedLogin";
        User updatedUser = new User(
                "Adam",
                "Nowak",
                login,
                "a.nowak@mail.com"
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));
        given(userRepository.existsByUsername(login)).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.putUpdateUser(userId, updatedUser))
                .isInstanceOf(UsernameAlreadyTakenException.class)
                .hasMessageContaining("Username already taken!");

        verify(userRepository, never()).save(any());
    }
}
