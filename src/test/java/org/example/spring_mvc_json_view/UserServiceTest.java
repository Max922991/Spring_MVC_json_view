package org.example.spring_mvc_json_view;

import jakarta.persistence.EntityNotFoundException;
import org.example.spring_mvc_json_view.models.User;
import org.example.spring_mvc_json_view.repositories.UserRepository;
import org.example.spring_mvc_json_view.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setName("Ivan Ivanov");
        user.setEmail("ivan.ivanov@example.com");
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("Ivan Ivanov", users.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById_UserFound() {
        when(userRepository.findUserByIdWithOrders(1L)).thenReturn(user);

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("Ivan Ivanov", foundUser.getName());
        verify(userRepository, times(1)).findUserByIdWithOrders(1L);
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findUserByIdWithOrders(1L)).thenReturn(null);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User with id 1 not found", exception.getMessage());
        verify(userRepository, times(1)).findUserByIdWithOrders(1L);
    }

    @Test
    public void testCreateUser() {
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("Ivan Ivanov", createdUser.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser_UserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User updatedUserDetails = new User();
        updatedUserDetails.setName("Jane Doe");
        updatedUserDetails.setEmail("jane.doe@example.com");

        when(userRepository.save(any(User.class))).thenReturn(updatedUserDetails);

        User updatedUser = userService.updateUser(1L, updatedUserDetails);

        assertNotNull(updatedUser);
        assertEquals("Jane Doe", updatedUser.getName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.updateUser(1L, user);
        });

        assertEquals("User with id 1 not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteUser_UserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User with id 1 not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }
}
