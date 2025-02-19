package com.example.SpringBootMicroservice.service.impl;

import com.example.SpringBootMicroservice.dto.request.RoleRequest;
import com.example.SpringBootMicroservice.dto.response.RoleDto;
import com.example.SpringBootMicroservice.entity.Role;
import com.example.SpringBootMicroservice.exception.ResourceNotFoundException;
import com.example.SpringBootMicroservice.exception.RoleNameTakenException;
import com.example.SpringBootMicroservice.mapper.RoleMapper;
import com.example.SpringBootMicroservice.repository.IRoleRepository;
import com.example.SpringBootMicroservice.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

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
            throw new RoleNameTakenException("This Rol already exists");
        } else {
            Role newRole = RoleMapper.mapper.requestToDto(role);
            return RoleMapper.mapper.toDto(roleRepository.save(newRole));
        }
    }

    @Override
    public Optional<RoleDto> findById(Long id) throws ResourceNotFoundException {
        return Optional.empty();
    }

    @Override
    public RoleDto update(Long id, RoleRequest role) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {

    }
}
