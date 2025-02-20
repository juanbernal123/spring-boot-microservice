package com.example.SpringBootMicroservice.service;

import com.example.SpringBootMicroservice.dto.request.RoleRequest;
import com.example.SpringBootMicroservice.dto.response.RoleDto;
import com.example.SpringBootMicroservice.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    List<RoleDto> findAll();
    RoleDto save(RoleRequest role) throws Exception;
    Optional<RoleDto> findById(Long id) throws ResourceNotFoundException;
    RoleDto update(Long id, RoleRequest role) throws Exception;
    void delete(Long id) throws ResourceNotFoundException;
}
