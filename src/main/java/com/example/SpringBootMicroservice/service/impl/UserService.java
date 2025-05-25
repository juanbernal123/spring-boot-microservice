package com.example.SpringBootMicroservice.service.impl;

import com.example.SpringBootMicroservice.dto.request.UserRequest;
import com.example.SpringBootMicroservice.dto.response.UserDto;
import com.example.SpringBootMicroservice.entity.User;
import com.example.SpringBootMicroservice.exception.EmailTakenException;
import com.example.SpringBootMicroservice.exception.ResourceNotFoundException;
import com.example.SpringBootMicroservice.exception.UserRegistrationException;
import com.example.SpringBootMicroservice.exception.UsernameTakenException;
import com.example.SpringBootMicroservice.mapper.UserMapper;
import com.example.SpringBootMicroservice.repository.IUserRepository;
import com.example.SpringBootMicroservice.service.IUserService;
import lombok.AllArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<UserDto> findAll() {
        return UserMapper.mapper.toDtoList(userRepository.findAll());
    }

    @Override
    public UserDto save(UserRequest user) throws Exception {
        List<String> errors = new ArrayList<>();

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            errors.add("Ya existe este usuario.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            errors.add("Ya existe este email.");
        }

        if (!errors.isEmpty()) {
            throw new UserRegistrationException(errors);
        }

        User newUser = UserMapper.mapper.requestToEntity(user);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return UserMapper.mapper.toDto(userRepository.save(newUser));

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
        if (userEntity.isEmpty()) {
            throw new ResourceNotFoundException("No existe este usuario: " + id);
        }

        User updateUser = userEntity.get();

        // Validar si el username ha cambiado y si ya está en uso
        if (!updateUser.getUsername().equals(user.getUsername()) && userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameTakenException("El nombre de usuario ya está en uso.");
        }

        // Validar si el email ha cambiado y si ya está en uso
        if (!updateUser.getEmail().equals(user.getEmail()) && userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailTakenException("El email ya está en uso.");
        }

        // Actualizar campos solo si no son nulos o vacíos
        if (user.getUsername() != null && !user.getUsername().isBlank()) {
            updateUser.setUsername(user.getUsername());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            updateUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        updateUser.setUpdatedAt(LocalDateTime.now());
        return UserMapper.mapper.toDto(userRepository.save(updateUser));
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

    @Override
    public byte[] exportUsersToExcel() {
        List<UserDto> users = findAll();

        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet("Users");

            // Header
            XSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("Username");
            row.createCell(2).setCellValue("Email");

            // Datos
            int colIndex = 0;
            int rowIndex = 1;
            for (UserDto user : users) {
                row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getName());
                row.createCell(2).setCellValue(user.getEmail());

                // Autoajustar las celdas
                sheet.autoSizeColumn(colIndex++);
                sheet.autoSizeColumn(colIndex++);
                sheet.autoSizeColumn(colIndex++);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new IllegalStateException("Error generating Excel file", e);
        }
    }

}
