package com.example.SpringBootMicroservice.service;

import com.example.SpringBootMicroservice.dto.request.UserRequest;
import com.example.SpringBootMicroservice.dto.response.UserDto;
import com.example.SpringBootMicroservice.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<UserDto> findAll();
    UserDto save(UserRequest user) throws Exception;
    Optional<UserDto> findById(Long id) throws ResourceNotFoundException;
    UserDto update(Long id, UserRequest user) throws Exception;
    void delete(Long id) throws ResourceNotFoundException;
    Optional<UserDto> findByUsername(String username);
    Optional<UserDto> findByEmail(String email);
    byte[] exportUsersToExcel();
}
