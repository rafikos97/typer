package pl.rafiki.typer.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.rafiki.typer.security.models.Role;
import pl.rafiki.typer.security.repositories.RoleRepository;
import pl.rafiki.typer.user.exceptions.EmailAddressAlreadyTakenException;
import pl.rafiki.typer.user.exceptions.IncorrectPasswordException;
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

    private UserService underTest;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private User user;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, passwordEncoder, roleRepository);
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
                "username",
                "a.tester@mail.com",
                "password",
                null
        );

        given(roleRepository.findByAuthority(any())).willReturn(Optional.of(new Role()));

        // when
        when(passwordEncoder.encode(any())).thenReturn(user.getPassword());
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
                "username",
                "a.tester@mail.com",
                "password",
                null
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
                "username",
                "a.tester@mail.com",
                "password",
                null
        );

        given(userRepository.existsByUsername(user.getUsername())).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.registerUser(user))
                .isInstanceOf(UsernameAlreadyTakenException.class)
                .hasMessageContaining("Login already taken!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void canUpdateUser() {
        // given
        Long userId = 1L;
        User updatedUser = new User(
                "Andrew",
                "Tester",
                "username",
                "a.tester@mail.com",
                null,
                null
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
                "Andrew",
                "Tester",
                "username",
                "a.tester@mail.com",
                null,
                null
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
                "Andrew",
                "Tester",
                "username",
                email,
                null,
                null
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
    void willThrowWhenEmailLoginIsAlreadyTakenDuringUpdate() {
        // given
        Long userId = 1L;
        String username = "updatedUsername";
        User updatedUser = new User(
                "Andrew",
                "Tester",
                username,
                "a.tester@mail.com",
                null,
                null
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));
        given(userRepository.existsByUsername(username)).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.putUpdateUser(userId, updatedUser))
                .isInstanceOf(UsernameAlreadyTakenException.class)
                .hasMessageContaining("Username already taken!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void canPatchUpdateUser() {
        // given
        Long userId = 1L;
        UserDTO updatedUser = new UserDTO(
                "Andrew",
                "Tester",
                "testUsername",
                "a.tester@mail.com"
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));

        // when
        underTest.patchUpdateUser(userId, updatedUser);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getFirstName()).isEqualTo(updatedUser.getFirstName());
        assertThat(capturedUser.getLastName()).isEqualTo(updatedUser.getLastName());
        assertThat(capturedUser.getUsername()).isEqualTo(updatedUser.getUsername());
        assertThat(capturedUser.getEmail()).isEqualTo(updatedUser.getEmail());
    }

    @Test
    void willThrowWhenUserDoesNotExistDuringPatchUpdate() {
        // given
        Long userId = 1L;
        UserDTO updatedUser = new UserDTO(
                "Andrew",
                "Tester",
                "testUsername",
                "a.tester@mail.com"
        );

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.patchUpdateUser(userId, updatedUser))
                .isInstanceOf(UserDoesNotExistException.class)
                .hasMessageContaining("User with id: " + userId + " does not exist!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void willThrowWhenEmailAddressIsAlreadyTakenDuringPatchUpdate() {
        // given
        Long userId = 1L;
        String email = "a.nowak@mail.com";
        UserDTO updatedUser = new UserDTO(
                "Andrew",
                "Tester",
                "testUsername",
                email
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));
        given(userRepository.existsByEmail(email)).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.patchUpdateUser(userId, updatedUser))
                .isInstanceOf(EmailAddressAlreadyTakenException.class)
                .hasMessageContaining("Email address already taken!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void willThrowWhenUsernameIsAlreadyTakenDuringPatchUpdate() {
        // given
        Long userId = 1L;
        String username = "updatedUsername";
        UserDTO updatedUser = new UserDTO(
                "Andrew",
                "Tester",
                username,
                "a.tester@mail.com"
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));
        given(userRepository.existsByUsername(username)).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.patchUpdateUser(userId, updatedUser))
                .isInstanceOf(UsernameAlreadyTakenException.class)
                .hasMessageContaining("Username already taken!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void canChangePassword() {
        // given
        Long userId = 1L;

        PasswordDTO passwordDTO = new PasswordDTO(
                "oldPassword",
                "newPassword"
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(any(), any())).willReturn(true);

        // when
        underTest.updatePassword(userId, passwordDTO);

        // then
        verify(user).setPassword(any());
    }

    @Test
    void willThrowWhenUserDoesNotExistWhileChangingPassword() {
        // given
        Long userId = 1L;

        PasswordDTO passwordDTO = new PasswordDTO(
                "oldPassword",
                "newPassword"
        );

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.updatePassword(userId, passwordDTO))
                .isInstanceOf(UserDoesNotExistException.class)
                .hasMessageContaining("User with id: " + userId + " does not exist!");

        verify(user, never()).setPassword(any());
    }

    @Test
    void willThrowWhenOldPasswordIsIncorrectWhileChangingPassword() {
        // given
        Long userId = 1L;

        PasswordDTO passwordDTO = new PasswordDTO(
                "oldPassword",
                "newPassword"
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(any(), any())).willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.updatePassword(userId, passwordDTO))
                .isInstanceOf(IncorrectPasswordException.class)
                .hasMessageContaining("Old password is incorrect!");

        verify(user, never()).setPassword(any());
    }
}
