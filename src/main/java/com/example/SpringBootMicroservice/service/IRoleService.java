package com.example.SpringBootMicroservice.service;

import com.example.SpringBootMicroservice.dto.request.RoleRequest;
import com.example.SpringBootMicroservice.dto.response.RoleDetailDto;
import com.example.SpringBootMicroservice.dto.response.RoleDto;
import com.example.SpringBootMicroservice.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    List<RoleDto> findAll();
    RoleDto save(RoleRequest role) throws Exception;
    Optional<RoleDetailDto> findById(Long id) throws ResourceNotFoundException;
    RoleDto update(Long id, RoleRequest role) throws Exception;
    RoleDto delete(Long id) throws ResourceNotFoundException;
    RoleDetailDto assignPermissionToRole(Long id, List<Long> permissions) throws Exception;
}
