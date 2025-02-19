package com.example.SpringBootMicroservice.service.impl;

import com.example.SpringBootMicroservice.dto.request.UserRequest;
import com.example.SpringBootMicroservice.dto.response.UserDto;
import com.example.SpringBootMicroservice.entity.User;
import com.example.SpringBootMicroservice.exception.ResourceNotFoundException;
import com.example.SpringBootMicroservice.exception.UsernameTakenException;
import com.example.SpringBootMicroservice.mapper.UserMapper;
import com.example.SpringBootMicroservice.repository.IUserRepository;
import com.example.SpringBootMicroservice.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<UserDto> findAll() {
        return UserMapper.mapper.toDtoList(userRepository.findAll());
    }

    @Override
    public UserDto save(UserRequest user) throws Exception {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            throw new UsernameTakenException("Ya existe este usuario");
        } else {
            User newUser = UserMapper.mapper.requestToDto(user);
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            return UserMapper.mapper.toDto(userRepository.save(newUser));
        }
    }

    @Override
    public Optional<UserDto> findById(Long id) throws ResourceNotFoundException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.map(UserMapper.mapper::toDto);
        } else {
            throw new ResourceNotFoundException("No existe este usuario: " + id);
        }
    }

    @Override
    public UserDto update(Long id, UserRequest user) throws Exception {
        Optional<User> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            User updateUser = userEntity.get();
            updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
            return UserMapper.mapper.toDto(userRepository.save(updateUser));
        } else {
            throw new ResourceNotFoundException("No existe este usuario: " + id);
        }
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Optional<User> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("No existe este usuario: " + id);
        }
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        Optional<User> findUser = userRepository.findByUsername(username);
        return findUser.map(UserMapper.mapper::toDto);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        return findUser.map(UserMapper.mapper::toDto);
    }
}
