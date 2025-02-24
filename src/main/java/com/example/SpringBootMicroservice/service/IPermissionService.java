package com.example.SpringBootMicroservice.service;

import com.example.SpringBootMicroservice.dto.request.PermissionRequest;
import com.example.SpringBootMicroservice.dto.response.PermissionDto;
import com.example.SpringBootMicroservice.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {
    List<PermissionDto> findAll();
    PermissionDto save(PermissionRequest permission) throws Exception;
    Optional<PermissionDto> findById(Long id) throws ResourceNotFoundException;
    PermissionDto update(Long id, PermissionRequest permission) throws Exception;
    void delete(Long id) throws ResourceNotFoundException;
    Optional<PermissionDto> findByName(String name);
}
