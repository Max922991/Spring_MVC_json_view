package org.example.spring_mvc_json_view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.example.spring_mvc_json_view.controllers.UserController;
import org.example.spring_mvc_json_view.models.User;
import org.example.spring_mvc_json_view.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
        user = new User();
        user.setId(1L);
        user.setName("Ivanov Ivan");
        user.setEmail("ivan.ivanov@example.com");
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ivanov Ivan"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testGetUserById_UserFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivanov Ivan"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    public void testGetUserById_UserNotFound() throws Exception {
        when(userService.getUserById(1L))
                .thenThrow(new EntityNotFoundException("User with id 1 not found"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    public void testCreateUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ivanov Ivan"));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    public void testUpdateUser_UserFound() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivanov Ivan"));

        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    public void testUpdateUser_UserNotFound() throws Exception {
        when(userService.updateUser(eq(1L),
                any(User.class))).thenThrow(new EntityNotFoundException("User with id 1 not found"));

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    public void testDeleteUser_UserFound() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    public void testDeleteUser_UserNotFound() throws Exception {
        doThrow(new EntityNotFoundException("User with id 1 not found"))
                .when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUser(1L);
    }
}
