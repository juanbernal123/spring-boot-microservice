package com.example.SpringBootMicroservice.service.impl;

import com.example.SpringBootMicroservice.dto.request.PermissionRequest;
import com.example.SpringBootMicroservice.dto.response.PermissionDto;
import com.example.SpringBootMicroservice.entity.Permission;
import com.example.SpringBootMicroservice.exception.ResourceNotFoundException;
import com.example.SpringBootMicroservice.mapper.PermissionMapper;
import com.example.SpringBootMicroservice.repository.IPermissionRepository;
import com.example.SpringBootMicroservice.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService implements IPermissionService {

    @Autowired
    private IPermissionRepository permissionRepository;

    @Override
    public List<PermissionDto> findAll() {
        return PermissionMapper.mapper.toDtoList(permissionRepository.findAll());
    }

    @Override
    public PermissionDto save(PermissionRequest permission) throws Exception {
       /* Optional<Permission> permissionOptional = permissionRepository.findByName(permission.getName());

        if (permissionOptional.isPresent()) {
            throw new ResourceNotFoundException("Permission already exists");
        } else {*/
            Permission newPermission = PermissionMapper.mapper.requestEntity(permission);
            return PermissionMapper.mapper.toDto(permissionRepository.save(newPermission));
        /*}*/
    }

    @Override
    public Optional<PermissionDto> findById(Long id) throws ResourceNotFoundException {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el registro: " + id));

        return Optional.ofNullable(PermissionMapper.mapper.toDto(permission));
    }

    @Override
    public PermissionDto update(Long id, PermissionRequest permission) throws Exception {
        Permission permissionEntity = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el registro: " + id));

        PermissionMapper.mapper.updateEntityFromRequest(permission, permissionEntity);
        return PermissionMapper.mapper.toDto(permissionRepository.save(permissionEntity));
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Permission permissionEntity = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el registro: " + id));
        permissionRepository.delete(permissionEntity);
    }

    @Override
    public Optional<PermissionDto> findByName(String name) {
        return Optional.empty();
    }
}
