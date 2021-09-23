package com.springbootflyway.repository;

import com.springbootflyway.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest extends DbContainerBaseTest {

    @Autowired private UserRepository userRepository;

    @BeforeEach
    public void setup() throws SQLException {
        //given
        userRepository.save(User.builder().id(1L).name("john").build());
        userRepository.save(User.builder().id(2L).name("doe").build());
    }


    @Test
    public void shouldGetUserById() {
        //when
        var persistedUser = userRepository.findById(1L);

        //then
        assertThat(persistedUser.isPresent()).isTrue();
        assertThat(persistedUser.get().getId()).isEqualTo(1);
        assertThat(persistedUser.get().getName()).isEqualTo("john");
    }

}