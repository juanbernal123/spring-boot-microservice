package com.example.SpringBootMicroservice.service.impl;

import com.example.SpringBootMicroservice.dto.request.RoleRequest;
import com.example.SpringBootMicroservice.dto.response.RoleDto;
import com.example.SpringBootMicroservice.entity.Role;
import com.example.SpringBootMicroservice.entity.User;
import com.example.SpringBootMicroservice.exception.ResourceNotFoundException;
import com.example.SpringBootMicroservice.exception.RoleNameTakenException;
import com.example.SpringBootMicroservice.mapper.RoleMapper;
import com.example.SpringBootMicroservice.mapper.UserMapper;
import com.example.SpringBootMicroservice.repository.IRoleRepository;
import com.example.SpringBootMicroservice.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

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
            System.out.println(newRole);
            return RoleMapper.mapper.toDto(roleRepository.save(newRole));
        }
    }

    @Override
    public Optional<RoleDto> findById(Long id) throws ResourceNotFoundException {
        Optional<Role> rolOptional = roleRepository.findById(id);
        if (rolOptional.isPresent()) {
            return rolOptional.map(RoleMapper.mapper::toDto);
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
    public void delete(Long id) throws ResourceNotFoundException {
        Optional<Role> roleEntity = roleRepository.findById(id);
        if (roleEntity.isPresent()) {
            roleRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("No existe este rol: " + id);
        }
    }
}
