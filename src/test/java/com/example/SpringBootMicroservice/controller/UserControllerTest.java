package com.example.SpringBootMicroservice.controller;

import com.example.SpringBootMicroservice.dto.request.UserRequest;
import com.example.SpringBootMicroservice.dto.response.UserDto;
import com.example.SpringBootMicroservice.service.impl.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto userDto;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("username");
        userDto.setCreated_at(String.valueOf(LocalDateTime.now()));
        userDto.setUpdated_at(String.valueOf(LocalDateTime.now()));

        userRequest = new UserRequest();
        userRequest.setUsername("username");
        userRequest.setEmail("user@example.com");
        userRequest.setPassword("123123");
    }


    @Test
    @Order(1)
    @DisplayName("Test: Obtener todos los usuarios")
    void findAll() throws Exception {
        when(userServiceImpl.findAll()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(userDto.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(userDto.getName())));
    }

    @Test
    @Order(2)
    @DisplayName("Test: Crear un usuario")
    void create() throws Exception {
        when(userServiceImpl.save(any(UserRequest.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(userDto.getName())));
    }

    @Test
    @Order(3)
    @DisplayName("Test: Buscar usuario por ID")
    void findById() throws Exception {
        when(userServiceImpl.findById(1L)).thenReturn(Optional.of(userDto));

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(userDto.getName())));
    }

    @Test
    @Order(4)
    @DisplayName("Test: Actualizar un usuario")
    void update() throws Exception {
        Long id = 1L;
        UserRequest updateRequest = new UserRequest();
        updateRequest.setUsername("username updated");
        updateRequest.setPassword("456456");

        UserDto updateUserDto = new UserDto();
        updateUserDto.setId(id);
        updateUserDto.setName("username updated");
        updateUserDto.setCreated_at(String.valueOf(LocalDateTime.now()));
        updateUserDto.setUpdated_at(String.valueOf(LocalDateTime.now()));

        when(userServiceImpl.update(id, updateRequest)).thenReturn(updateUserDto);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updateUserDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updateUserDto.getName())));
    }

    @Test
    @Order(15)
    @DisplayName("Test: Eliminar un usuario exitosamente")
    void deleteUser_Success() throws Exception {
        doNothing().when(userServiceImpl).delete(1L);

        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userServiceImpl, times(1)).delete(1L);
    }
}
