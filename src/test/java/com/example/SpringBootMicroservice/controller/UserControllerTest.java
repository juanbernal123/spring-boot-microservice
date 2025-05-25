package com.example.SpringBootMicroservice.controller;

import com.example.SpringBootMicroservice.dto.request.UserRequest;
import com.example.SpringBootMicroservice.dto.response.UserDto;
import com.example.SpringBootMicroservice.service.impl.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
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
        userDto.setEmail("user@example.com");
        userDto.setCreated_at(String.valueOf(LocalDateTime.now()));
        userDto.setUpdated_at(String.valueOf(LocalDateTime.now()));

        userRequest = new UserRequest();
        userRequest.setUsername("username");
        userRequest.setEmail("user@example.com");
        userRequest.setPassword("123123");
    }

    @Test
    @Order(1)
    @DisplayName("FindAll OK: Obtener todos los usuarios")
    void findAll() throws Exception {
        when(userServiceImpl.findAll()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(greaterThan(0)))
                .andExpect(jsonPath("$[0].id").value(userDto.getId().intValue()))
                .andExpect(jsonPath("$[0].name").value(userDto.getName()));
    }

    @Test
    @Order(2)
    @DisplayName("Create OK: Crear un usuario")
    void create() throws Exception {
        when(userServiceImpl.save(any(UserRequest.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId().intValue()))
                .andExpect(jsonPath("$.name").value(userDto.getName()));
    }

    @Test
    @Order(3)
    @DisplayName("FindById OK: Buscar usuario por ID")
    void findById() throws Exception {
        when(userServiceImpl.findById(1L)).thenReturn(Optional.of(userDto));

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId().intValue()))
                .andExpect(jsonPath("$.name").value(userDto.getName()));
    }

    @Test
    @Order(4)
    @DisplayName("Update OK: Actualizar un usuario")
    void update() throws Exception {
        Long id = 1L;
        UserRequest updateRequest = new UserRequest();
        updateRequest.setUsername("username updated");
        updateRequest.setPassword("456456");

        UserDto updatedDto = new UserDto();
        updatedDto.setId(id);
        updatedDto.setName("username updated");
        updatedDto.setCreated_at(String.valueOf(LocalDateTime.now()));
        updatedDto.setUpdated_at(String.valueOf(LocalDateTime.now()));

        when(userServiceImpl.update(id, updateRequest)).thenReturn(updatedDto);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedDto.getId().intValue()))
                .andExpect(jsonPath("$.name").value(updatedDto.getName()));
    }

    @Test
    @Order(5)
    @DisplayName("Delete OK: Eliminar un usuario")
    void deleteUser() throws Exception {
        doNothing().when(userServiceImpl).delete(1L);

        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userServiceImpl, times(1)).delete(1L);
    }

    @Test
    @Order(6)
    @DisplayName("Export OK: Descargar Excel de usuarios")
    void exportUsers_shouldReturnExcelFile() throws Exception {
        byte[] fakeExcel = "fake-excel-content".getBytes(StandardCharsets.UTF_8);
        when(userServiceImpl.exportUsersToExcel()).thenReturn(fakeExcel);

        mockMvc.perform(get("/users/export"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"users.xlsx\""))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(fakeExcel));
    }
}
