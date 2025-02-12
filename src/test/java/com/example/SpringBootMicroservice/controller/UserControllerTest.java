package com.example.SpringBootMicroservice.controller;

import com.example.SpringBootMicroservice.dto.request.UserRequest;
import com.example.SpringBootMicroservice.dto.response.UserDto;
import com.example.SpringBootMicroservice.service.impl.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userServiceImpl;
    private UserDto userDto;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("username");
        userDto.setCreated_at(LocalDateTime.now());
        userDto.setUpdated_at(LocalDateTime.now());

        userRequest = new UserRequest();
        userRequest.setUsername("username");
        userRequest.setPassword("123123");
    }

    @Test
    @Order(2)
    void findAll() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
        assertFalse(response.getResponse().getContentAsString().isEmpty());
    }

    @Test
    @Order(1)
    void create() throws Exception {
        when(userServiceImpl.save(any(UserRequest.class))).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.created_at").exists())
                .andExpect(jsonPath("$.updated_at").exists());
    }

    @Test
    @Order(3)
    void findById() throws Exception {
        when(userServiceImpl.findById(1L)).thenReturn(Optional.ofNullable(userDto));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.created_at").exists())
                .andExpect(jsonPath("$.updated_at").exists());
    }

    @Test
    @Order(4)
    void update() throws Exception {
        Long id = 1L;
        // actualizacion del usuario
        UserRequest updateRequest = new UserRequest();
        updateRequest.setUsername("username updated");
        updateRequest.setPassword("456456");

        // dto esperado
        UserDto updateUserDto = new UserDto();
        updateUserDto.setId(id);
        updateUserDto.setName("username updated");
        updateUserDto.setCreated_at(LocalDateTime.now());
        updateUserDto.setUpdated_at(LocalDateTime.now());

        when(userServiceImpl.update(eq(1L), any(UserRequest.class))).thenReturn(updateUserDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updateUserDto.getName()))
                .andExpect(jsonPath("$.created_at").exists())
                .andExpect(jsonPath("$.updated_at").exists());
    }

    @Test
    @Order(5)
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/users/1"))
                .andExpect(status().isOk());
    }
}