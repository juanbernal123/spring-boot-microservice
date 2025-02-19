package com.example.SpringBootMicroservice.controller;

import com.example.SpringBootMicroservice.dto.request.UserRequest;
import com.example.SpringBootMicroservice.dto.response.UserDto;
import com.example.SpringBootMicroservice.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userServiceImpl;

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
        userRequest.setEmail("");
        userRequest.setPassword("123123");
    }

    @Test
    @Order(2)
    void findAll() throws Exception {
        when(userServiceImpl.findAll()).thenReturn(List.of(userDto));
        ResponseEntity<List<UserDto>> response = userController.findAll();
        assertNotNull(response);
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals(userDto.getId(), response.getBody().get(0).getId());
    }

    @Test
    @Order(1)
    void create() throws Exception {
        when(userServiceImpl.save(any(UserRequest.class))).thenReturn(userDto);
        ResponseEntity<UserDto> response = userController.create(userRequest);
        assertNotNull(response);
        assertEquals(userDto.getId(), Objects.requireNonNull(response.getBody()).getId());
        assertEquals(userDto.getName(), response.getBody().getName());
    }

    @Test
    @Order(3)
    void findById() throws Exception {
        when(userServiceImpl.findById(1L)).thenReturn(Optional.of(userDto));
        ResponseEntity<UserDto> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(userDto.getId(), Objects.requireNonNull(response.getBody()).getId());
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
        updateUserDto.setCreated_at(String.valueOf(LocalDateTime.now()));
        updateUserDto.setUpdated_at(String.valueOf(LocalDateTime.now()));

        when(userServiceImpl.update(id, updateRequest)).thenReturn(updateUserDto);
        ResponseEntity<UserDto> response = userController.update(id, updateRequest);

        assertNotNull(response);
        assertEquals(updateUserDto.getId(), Objects.requireNonNull(response.getBody()).getId());
        assertEquals(updateUserDto.getName(), response.getBody().getName());
    }

    @Test
    @Order(5)
    void delete() throws Exception {
        doNothing().when(userServiceImpl).delete(1L);
        ResponseEntity<String> response = userController.delete(1L);
        assertNotNull(response);
        verify(userServiceImpl, times(1)).delete(1L);
    }
}