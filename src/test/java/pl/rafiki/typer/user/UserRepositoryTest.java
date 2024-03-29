package pl.rafiki.typer.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void existsByEmailTest() {
        // given
        String mail = "a.tester@mail.com";
        User user = new User(
                "Andrew",
                "Tester",
                "tester",
                mail,
                "password",
                null
        );
        underTest.save(user);

        // when
        boolean expected = underTest.existsByEmail(mail);

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void existsByUsernameTest() {
        //given
        String username = "tester";
        User user = new User(
                "Andrew",
                "Tester",
                username,
                "a.tester@mail.com",
                "password",
                null
        );
        underTest.save(user);

        // when
        boolean expected = underTest.existsByUsername(username);

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void findByUsernameTest() {
        //given
        String username = "tester";
        User user = new User(
                "Andrew",
                "Tester",
                username,
                "a.tester@mail.com",
                "password",
                null
        );
        underTest.save(user);

        // when
        Optional<User> userOptional = underTest.findUserByUsername(username);

        // then
        assertThat(userOptional).contains(user);
    }
}
