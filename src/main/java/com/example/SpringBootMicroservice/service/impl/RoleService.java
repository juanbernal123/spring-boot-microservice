package com.example.SpringBootMicroservice.service.impl;

import com.example.SpringBootMicroservice.dto.request.RoleRequest;
import com.example.SpringBootMicroservice.dto.response.RoleDetailDto;
import com.example.SpringBootMicroservice.dto.response.RoleDto;
import com.example.SpringBootMicroservice.entity.Permission;
import com.example.SpringBootMicroservice.entity.Role;
import com.example.SpringBootMicroservice.exception.ResourceNotFoundException;
import com.example.SpringBootMicroservice.exception.RoleNameTakenException;
import com.example.SpringBootMicroservice.mapper.RoleMapper;
import com.example.SpringBootMicroservice.repository.IPermissionRepository;
import com.example.SpringBootMicroservice.repository.IRoleRepository;
import com.example.SpringBootMicroservice.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private IPermissionRepository permissionRepository;
    @Autowired
    private MessageSource messageSource;

    @Override
    public List<RoleDto> findAll() {
        return RoleMapper.mapper.toDtoList(roleRepository.findAll());
    }

    @Override
    public RoleDto save(RoleRequest role) throws Exception {
        Optional<Role> roleOptional = roleRepository.findByName(role.getName());

        if (roleOptional.isPresent()) {
            throw new RoleNameTakenException("Ya existe este rol");
        } else {
            Role newRole = RoleMapper.mapper.requestToDto(role);
            return RoleMapper.mapper.toDto(roleRepository.save(newRole));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleDetailDto> findById(Long id) throws ResourceNotFoundException {
        Optional<Role> rolOptional = roleRepository.findWithPermissionsById(id);
        if (rolOptional.isPresent()) {
            Role role = rolOptional.get();
            role.getPermissions().size();
            return Optional.of(RoleMapper.mapper.detailToDto(role));
        } else {
            throw new ResourceNotFoundException("No existe este rol: " + id);
        }
    }

    @Override
    public RoleDto update(Long id, RoleRequest role) throws Exception {
        Optional<Role> roleEntity = roleRepository.findById(id);

        if (roleEntity.isEmpty()) {
            throw new ResourceNotFoundException("No existe este rol: " + id);
        }

        Role updateRole = roleEntity.get();

        // Verificar si el nombre ya existe en otro rol
        Optional<Role> roleOptional = roleRepository.findByName(role.getName());
        if (roleOptional.isPresent() && !roleOptional.get().getId().equals(id)) {
            throw new RoleNameTakenException("El nombre del rol ya está en uso.");
        }

        // Actualizar solo si el nombre no es nulo o vacío
        if (role.getName() != null && !role.getName().isBlank()) {
            updateRole.setName(role.getName());
        }

        updateRole.setUpdatedAt(LocalDateTime.now());

        return RoleMapper.mapper.toDto(roleRepository.save(updateRole));
    }

    @Override
    public RoleDto delete(Long id) throws ResourceNotFoundException {
        Optional<Role> roleEntity = roleRepository.findById(id);
        if (roleEntity.isPresent()) {
            roleRepository.deleteById(id);
            return RoleMapper.mapper.toDto(roleEntity.get());
        } else {
            throw new ResourceNotFoundException("No existe este rol: " + id);
        }
    }

    @Override
    public RoleDetailDto assignPermissionToRole(Long id, List<Long> permissionIds) throws Exception {
        String notFoundMessage = messageSource.getMessage("notFound.message", new Object[]{id}, Locale.getDefault());
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));

        List<Permission> permissions = permissionRepository.findAllById(permissionIds);

        if (permissions.size() != permissionIds.size()) {
            String errorMessage = messageSource.getMessage("permissions.notFound.message", null, Locale.getDefault());
            throw new ResourceNotFoundException(errorMessage);
        }

        role.setPermissions(permissions);
        Role updatedRole = roleRepository.save(role);
        return RoleMapper.mapper.detailToDto(updatedRole);
    }
}
