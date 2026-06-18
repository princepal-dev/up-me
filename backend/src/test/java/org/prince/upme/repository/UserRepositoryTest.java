package org.prince.upme.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.prince.upme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void existsByEmail() {
        // given
        User user = new User();
        String email = "test@test.com";
        user.setUserName("test");
        user.setEmail(email);
        user.setSignUpMethod("Email");

        underTest.saveAndFlush(user);

        // when
        boolean foundUser = underTest.existsByEmail(email);

        // then
        assertThat(foundUser).isEqualTo(true);
    }

    @Test
    void findByEmail() {
        // given
        String email = "test@test.com";

        User user = new User();
        user.setUserName("test");
        user.setEmail(email);
        user.setSignUpMethod("Email");

        underTest.saveAndFlush(user);

        // when
        Optional<User> foundUser = underTest.findByEmail(email);

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void findByUserName() {
        // given
        String email = "test@test.com";
        String userName = "test";

        User user = new User();
        user.setEmail(email);
        user.setUserName(userName);
        user.setSignUpMethod("Email");

        underTest.saveAndFlush(user);

        // when
        Optional<User> foundUser = underTest.findByUserName(userName);

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void existsByUserName() {
        // given
        User user = new User();
        String userName = "test";

        String email = "test@test.com";
        user.setUserName(userName);
        user.setEmail(email);
        user.setSignUpMethod("Email");

        underTest.saveAndFlush(user);

        // when
        boolean foundUser = underTest.existsByUserName(userName);

        // then
        assertThat(foundUser).isEqualTo(true);
    }
}