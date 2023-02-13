package io.modicon.smartixtask.domain.repository;

import io.modicon.smartixtask.domain.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByUsername_whenUserExist() {
        String telephone = "telephone";
        UserEntity user = UserEntity.builder()
                .telephone(telephone)
                .password("password")
                .build();

        userRepository.save(user);
        Optional<UserEntity> byTelephone = userRepository.findByTelephone(telephone);
        boolean expected = byTelephone.isPresent();
        assertTrue(expected);
        assertEquals(byTelephone.get(), user);
    }

    @Test
    void shouldNotFindUserByUsername_whenUserNotExist() {
        String telephone = "telephone";
        Optional<UserEntity> byTelephone = userRepository.findByTelephone(telephone);
        boolean expected = byTelephone.isPresent();
        assertFalse(expected);
    }
}