package com.example.SpringBootMicroservice.controller;

import com.example.SpringBootMicroservice.configuration.JwtAuthenticationFilter;
import com.example.SpringBootMicroservice.dto.request.PermissionRequest;
import com.example.SpringBootMicroservice.dto.response.PermissionDto;
import com.example.SpringBootMicroservice.service.impl.PermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PermissionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
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
    @DisplayName("Test: obtener todos los permisos")
    void findAll() throws Exception {
        when(permissionServiceImpl.findAll()).thenReturn(List.of(permissionDto));

        mockMvc.perform(get("/permissions")
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
    void create() throws Exception {
        when(permissionServiceImpl.save(any(PermissionRequest.class))).thenReturn(permissionDto);

        mockMvc.perform(post("/permissions")
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
    void findById() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}