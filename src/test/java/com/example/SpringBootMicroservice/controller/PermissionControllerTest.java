package com.example.SpringBootMicroservice.controller;

import com.example.SpringBootMicroservice.dto.request.PermissionRequest;
import com.example.SpringBootMicroservice.dto.response.PermissionDto;
import com.example.SpringBootMicroservice.service.impl.PermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PermissionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PermissionService permissionServiceImpl;
    @Autowired
    private ObjectMapper objectMapper;

    private PermissionDto permissionDto;
    private PermissionRequest permissionRequest;

    @BeforeEach
    void setUp() {
        permissionDto = new PermissionDto();
        permissionDto.setId(1L);
        permissionDto.setName("test");
        permissionDto.setDisplay_name("test display name");
        permissionDto.setCreated_at(String.valueOf(LocalDateTime.now()));
        permissionDto.setUpdated_at(String.valueOf(LocalDateTime.now()));

        permissionRequest = new PermissionRequest();
        permissionRequest.setName("test");
        permissionRequest.setDisplay_name("test display name");
    }


    @Test
    @DisplayName("FindAll OK: obtener todos los permisos")
    void findAll() throws Exception {
        when(permissionServiceImpl.findAll()).thenReturn(List.of(permissionDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/permissions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(permissionDto.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(permissionDto.getName())))
                .andExpect(jsonPath("$[0].display_name", is(permissionDto.getDisplay_name())))
                .andExpect(jsonPath("$[0].created_at", is(permissionDto.getCreated_at())))
                .andExpect(jsonPath("$[0].updated_at", is(permissionDto.getUpdated_at())));
        ;
    }

    @Test
    @DisplayName("Create OK: crear un registro con exito")
    void create() throws Exception {
        when(permissionServiceImpl.save(any(PermissionRequest.class))).thenReturn(permissionDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(permissionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(permissionDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(permissionDto.getName())))
                .andExpect(jsonPath("$.display_name", is(permissionDto.getDisplay_name())))
                .andExpect(jsonPath("$.created_at", is(permissionDto.getCreated_at())))
                .andExpect(jsonPath("$.updated_at", is(permissionDto.getUpdated_at())));
    }

    @Test
    @DisplayName("FindById OK: buscar un registro por ID")
    void findById() throws Exception {
        when(permissionServiceImpl.findById(1L)).thenReturn(Optional.of(permissionDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/permissions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(permissionDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(permissionDto.getName())))
                .andExpect(jsonPath("$.display_name", is(permissionDto.getDisplay_name())))
                .andExpect(jsonPath("$.created_at", is(permissionDto.getCreated_at())))
                .andExpect(jsonPath("$.updated_at", is(permissionDto.getUpdated_at())));
    }

    @Test
    @DisplayName("Update OK: actualizacion de permiso mediante ID")
    void update() throws Exception {
        Long id = 1L;
        PermissionRequest updateRequest = new PermissionRequest();
        updateRequest.setName("test updated");
        updateRequest.setDisplay_name("display name updated");

        PermissionDto updateDto = new PermissionDto();
        updateDto.setId(id);
        updateDto.setName("test updated");
        updateDto.setDisplay_name("display name updated");
        updateDto.setCreated_at(String.valueOf(LocalDateTime.now()));
        updateDto.setUpdated_at(String.valueOf(LocalDateTime.now()));

        when(permissionServiceImpl.update(id, updateRequest)).thenReturn(updateDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/permissions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updateDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updateDto.getName())))
                .andExpect(jsonPath("$.display_name", is(updateDto.getDisplay_name())))
                .andExpect(jsonPath("$.created_at", is(updateDto.getCreated_at())))
                .andExpect(jsonPath("$.updated_at", is(updateDto.getUpdated_at())));
    }

    @Test
    @DisplayName("Delete OK: eliminar un permiso mediante ID")
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/permissions/1"))
                .andExpect(status().isOk());
    }
}