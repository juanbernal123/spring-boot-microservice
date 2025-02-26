package com.example.SpringBootMicroservice.controller;

import com.example.SpringBootMicroservice.dto.request.RoleRequest;
import com.example.SpringBootMicroservice.dto.response.RoleDetailDto;
import com.example.SpringBootMicroservice.dto.response.RoleDto;
import com.example.SpringBootMicroservice.service.impl.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService roleServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    private RoleDto roleDto;
    private RoleDetailDto roleDetailDto;
    private RoleRequest roleRequest;

    @BeforeEach
    void setUp() {
        roleDto = new RoleDto();
        roleDto.setId(1L);
        roleDto.setName("ROLE_USER");
        roleDto.setCreated_at(String.valueOf(LocalDateTime.now()));
        roleDto.setUpdated_at(String.valueOf(LocalDateTime.now()));

        roleDetailDto = new RoleDetailDto();
        roleDetailDto.setId(1L);
        roleDetailDto.setName("ROLE_USER");
        roleDetailDto.setPermissions(new HashSet<>());
        roleDetailDto.setCreated_at(String.valueOf(LocalDateTime.now()));
        roleDetailDto.setUpdated_at(String.valueOf(LocalDateTime.now()));

        roleRequest = new RoleRequest();
        roleRequest.setName("ROLE_USER");
    }

    @Test
    @Order(1)
    @DisplayName("FindAll OK: Obtener todos los roles")
    void findAll() throws Exception {
        when(roleServiceImpl.findAll()).thenReturn(List.of(roleDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/roles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()").value(greaterThan(0)))
                .andExpect(jsonPath("$.data[0].id").value(roleDto.getId().intValue()))
                .andExpect(jsonPath("$.data[0].name").value(roleDto.getName()));
    }

    @Test
    @Order(2)
    @DisplayName("Create OK: Crear un rol")
    void create() throws Exception {
        when(roleServiceImpl.save(any(RoleRequest.class))).thenReturn(roleDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(roleDto.getId().intValue()))
                .andExpect(jsonPath("$.data.name").value(roleDto.getName()));
    }

    @Test
    @Order(3)
    @DisplayName("FindById OK: Buscar rol por ID")
    void findById() throws Exception {
        when(roleServiceImpl.findById(1L)).thenReturn(Optional.of(roleDetailDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/roles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(roleDto.getId().intValue()))
                .andExpect(jsonPath("$.data.name").value(roleDto.getName()));
    }

    @Test
    @Order(4)
    @DisplayName("Update OK: Actualizar un rol")
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

        mockMvc.perform(MockMvcRequestBuilders.put("/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(updateRoleDto.getId().intValue()))
                .andExpect(jsonPath("$.data.name").value(updateRoleDto.getName()));
    }

    @Test
    @Order(15)
    @DisplayName("Delete OK: Eliminar un rol exitosamente")
    void delete() throws Exception {
        when(roleServiceImpl.delete(1L)).thenReturn(roleDto);

        mockMvc.perform(MockMvcRequestBuilders.delete("/roles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(roleServiceImpl, times(1)).delete(1L);
    }
}