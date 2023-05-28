package com.testing.UserTestProject.services;

import com.testing.UserTestProject.model.User;
import com.testing.UserTestProject.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    void getAllUsersAndReturnsListOfUsers() {
        User user = new User(5L, "Name1", "11111111", "test@test.bg");
        User user2 = new User(2L, "Name2", "2222222", "test2@test.bg");
        List<User> userList = Arrays.asList(user, user2);

        when(userRepository.findAll()).thenReturn(userList);
        List<User> result = userService.getAllUsers();

        assertEquals(result.size(), 2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findUSerByIdWhenExist() {
        Long userId = 5L;
        User user = new User(userId, "Name1", "11111111", "test@test.bg");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User actual = userService.getUserById(userId);

        assertThat(actual).isEqualTo(user);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserByIdIfNotExistThrowException() {
        Long userId = 100L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> userService.getUserById(userId))
                .withMessage("User not found with id: " + userId);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findUserByEmailIfExistReturnUser() {
        String email = "test@test.abv";
        User user = new User(1L, "TestName", "123123123", email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        User actual = userService.findUserByEmail(email);

        assertThat(actual).isEqualTo(user);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findUserByEmailIfNotExistThenThrowException() {
        String email = "test@test.abv";
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> userService.findUserByEmail(email))
                .withMessage("User not found with email: " + email);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void updateUserIfExistThenReturnUpdatedUser() {
        Long userId = 5L;
        User existingUser = new User(5L, "Name1", "11111111", "test@test.bg");
        User updatedUser = new User(2L, "Name2", "2222222", "test2@test.bg");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        User result = userService.updateUser(userId, updatedUser);

        assertThat(result).isEqualTo(updatedUser);
        assertThat(result.getName()).isEqualTo("Name2");
        assertThat(result.getPhone()).isEqualTo("2222222");
        assertThat(result.getEmail()).isEqualTo("test2@test.bg");
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUserIfNotExistThenThrowException() {
        Long userId = 100L;
        User updatedUser = new User(userId, "TEstName", "123123123", "tset@test.abv");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> userService.updateUser(userId, updatedUser))
                .withMessage("User not found with id: " + userId);
        verify(userRepository, times(1)).findById(userId);
    }
}