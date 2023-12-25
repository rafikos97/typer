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
import pl.rafiki.typer.user.exceptions.*;

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
        RegisterUserDTO user = new RegisterUserDTO(
                "Andrew",
                "Tester",
                "username",
                "a.tester@mail.com",
                "Password123#"
        );

        given(roleRepository.findByAuthority(any())).willReturn(Optional.of(new Role()));

        // when
        when(passwordEncoder.encode(any())).thenReturn(user.getPassword());
        underTest.registerUser(user);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        RegisterUserDTO capturedUserAsRegisterUserDTO = new RegisterUserDTO(capturedUser.getFirstName(), capturedUser.getLastName(), capturedUser.getUsername(), capturedUser.getEmail(), capturedUser.getPassword());
        assertThat(capturedUserAsRegisterUserDTO).isEqualTo(user);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        RegisterUserDTO user = new RegisterUserDTO(
                "Andrew",
                "Tester",
                "username",
                "a.tester@mail.com",
                "Password123#"
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
        RegisterUserDTO user = new RegisterUserDTO(
                "Andrew",
                "Tester",
                "username",
                "a.tester@mail.com",
                "Password123#"
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
    void willThrowWhenEmailDoesNotMatchRegex() {
        // given
        String email = "a.tester$mail.com";
        RegisterUserDTO user = new RegisterUserDTO(
                "Andrew",
                "Tester",
                "username",
                email,
                "Password123#"
        );

        // when
        // then
        assertThatThrownBy(() -> underTest.registerUser(user))
                .isInstanceOf(InvalidEmailException.class)
                .hasMessageContaining("Provided email is invalid!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void willThrowWhenPasswordDoesNotMeetThePasswordPolicy() {
        // given
        String password = "password";
        RegisterUserDTO user = new RegisterUserDTO(
                "Andrew",
                "Tester",
                "username",
                "a.tester@mail.com",
                password
        );

        // when
        // then
        assertThatThrownBy(() -> underTest.registerUser(user))
                .isInstanceOf(PasswordDoesNotMatchPatternException.class)
                .hasMessageContaining("Provided password does not meet password policy!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void canUpdateUser() {
        // given
        Long userId = 1L;
        UserDTO updatedUser = new UserDTO (
                "Andrew",
                "Tester",
                "username",
                "a.tester@mail.com"
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));

        // when
        underTest.putUpdateUser(userId, updatedUser);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        UserDTO capturedUserDTO = new UserDTO(capturedUser.getFirstName(), capturedUser.getLastName(), capturedUser.getUsername(), capturedUser.getEmail());
        assertThat(capturedUserDTO).isEqualTo(updatedUser);
    }

    @Test
    void willThrowWhenUserDoesNotExistDuringUpdate() {
        // given
        Long userId = 1L;
        UserDTO updatedUser = new UserDTO (
                "Andrew",
                "Tester",
                "username",
                "a.tester@mail.com"
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
        UserDTO updatedUser = new UserDTO (
                "Andrew",
                "Tester",
                "username",
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
    void willThrowWhenEmailLoginIsAlreadyTakenDuringUpdate() {
        // given
        Long userId = 1L;
        String username = "updatedUsername";
        UserDTO updatedUser = new UserDTO (
                "Andrew",
                "Tester",
                username,
                "a.tester@mail.com"
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
    void willThrowWhenEmailDoesNotMatchRegexDuringUpdate() {
        // given
        Long userId = 1L;
        String email = "a.tester$mail.com";
        UserDTO updatedUser = new UserDTO(
                "Andrew",
                "Tester",
                "username",
                email
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(new User()));

        // when
        // then
        assertThatThrownBy(() -> underTest.putUpdateUser(userId, updatedUser))
                .isInstanceOf(InvalidEmailException.class)
                .hasMessageContaining("Provided email is invalid!");

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
                "oldPassword123#",
                "newPassword123#"
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
                "oldPassword123#",
                "newPassword123#"
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
                "oldPassword123#",
                "newPassword123#"
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

    @Test
    void willThrowWhenNewPasswordDoesNotMeetThePasswordPolicyWhileChangingPassword() {
        // given
        Long userId = 1L;

        PasswordDTO passwordDTO = new PasswordDTO(
                "oldPassword123#",
                "password"
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(any(), any())).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.updatePassword(userId, passwordDTO))
                .isInstanceOf(PasswordDoesNotMatchPatternException.class)
                .hasMessageContaining("Provided password does not meet password policy!");

        verify(user, never()).setPassword(any());
    }

    @Test
    void canDeleteUser() {
        // given
        Long userId = 1L;

        given(userRepository.existsById(userId)).willReturn(true);

        // when
        underTest.deleteUser(userId);

        // then
        verify(userRepository, times(1)).deleteById(any());
    }

    @Test
    void willThrowWhenUserDoesNotExistWhileDeletingUser() {
        // given
        Long userId = 1L;

        given(userRepository.existsById(userId)).willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteUser(userId))
                .isInstanceOf(UserDoesNotExistException.class)
                .hasMessageContaining("User with id: " + userId + " does not exist!");

        verify(userRepository, never()).deleteById(any());
    }
}
