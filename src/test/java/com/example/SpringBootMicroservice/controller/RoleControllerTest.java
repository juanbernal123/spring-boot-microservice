package com.example.SpringBootMicroservice.controller;

import com.example.SpringBootMicroservice.configuration.JwtAuthenticationFilter;
import com.example.SpringBootMicroservice.dto.request.RoleRequest;
import com.example.SpringBootMicroservice.dto.response.RoleDto;
import com.example.SpringBootMicroservice.service.impl.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RoleController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class),
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    private RoleDto roleDto;
    private RoleRequest roleRequest;

    @BeforeEach
    void setUp() {
        roleDto = new RoleDto();
        roleDto.setId(1L);
        roleDto.setName("ROLE_USER");
        roleDto.setCreated_at(String.valueOf(LocalDateTime.now()));
        roleDto.setUpdated_at(String.valueOf(LocalDateTime.now()));

        roleRequest = new RoleRequest();
        roleRequest.setName("ROLE_USER");
    }
    @Test
    @Order(1)
    @DisplayName("Test: Obtener todos los roles")
    void findAll() throws Exception {
        when(roleServiceImpl.findAll()).thenReturn(List.of(roleDto));

        mockMvc.perform(get("/roles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(roleDto.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(roleDto.getName())));
    }

    @Test
    @Order(2)
    @DisplayName("Test: Crear un rol")
    void create() throws Exception {
        when(roleServiceImpl.save(any(RoleRequest.class))).thenReturn(roleDto);

        mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(roleDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(roleDto.getName())));
    }

    @Test
    @Order(3)
    @DisplayName("Test: Buscar rol por ID")
    void findById() throws Exception {
        when(roleServiceImpl.findById(1L)).thenReturn(Optional.of(roleDto));

        mockMvc.perform(get("/roles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(roleDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(roleDto.getName())));
    }

    @Test
    @Order(4)
    @DisplayName("Test: Actualizar un rol")
    void update() throws Exception {
        Long id = 1L;
        RoleRequest updateRequest = new RoleRequest();
        updateRequest.setName("role_updated");

        RoleDto updateRoleDto = new RoleDto();
        updateRoleDto.setId(id);
        updateRoleDto.setName("username updated");
        updateRoleDto.setCreated_at(String.valueOf(LocalDateTime.now()));
        updateRoleDto.setUpdated_at(String.valueOf(LocalDateTime.now()));

        when(roleServiceImpl.update(id, updateRequest)).thenReturn(updateRoleDto);

        mockMvc.perform(put("/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updateRoleDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updateRoleDto.getName())));
    }

    @Test
    @Order(15)
    @DisplayName("Test: Eliminar un rol exitosamente")
    void deleteRole_Success() throws Exception {
        doNothing().when(roleServiceImpl).delete(1L);

        mockMvc.perform(delete("/roles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(roleServiceImpl, times(1)).delete(1L);
    }
}