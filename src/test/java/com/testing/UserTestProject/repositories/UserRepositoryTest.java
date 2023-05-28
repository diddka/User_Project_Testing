package com.testing.UserTestProject.repositories;

import com.testing.UserTestProject.model.User;
import com.testing.UserTestProject.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class UserRepositoryTest {
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findUserByEmailIfExist() {
        String email = "test@test.bg";
        User user = new User(5l, "Name", "11111111", email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        User expected = userRepository.findByEmail(email);

        assertThat(user).isEqualTo(expected);
        verify(userRepository, times(1)).findByEmail(email);
    }
    @Test
    void findUserByEmailIfNotExistThrowException() {
        String email = "nonexisting@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> userService.findUserByEmail(email))
                .withMessage("User not found with email: " + email);
        verify(userRepository, times(1)).findByEmail(email);
    }
}