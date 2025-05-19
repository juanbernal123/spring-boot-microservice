package com.example.SpringBootMicroservice.service.impl;

import com.example.SpringBootMicroservice.dto.request.UserRequest;
import com.example.SpringBootMicroservice.dto.response.UserDto;
import com.example.SpringBootMicroservice.entity.User;
import com.example.SpringBootMicroservice.exception.ResourceNotFoundException;
import com.example.SpringBootMicroservice.exception.UserRegistrationException;
import com.example.SpringBootMicroservice.exception.UsernameTakenException;
import com.example.SpringBootMicroservice.mapper.UserMapper;
import com.example.SpringBootMicroservice.repository.IUserRepository;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new User();
        userEntity.setId(1L);
        userEntity.setUsername("username");
        userEntity.setEmail("user@example.com");
        userEntity.setPassword("encrypted-pass");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("username");
        userDto.setEmail("user@example.com");

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("username");
        userRequest.setEmail("user@example.com");
        userRequest.setPassword("123123");
    }

    @Test
    void findAll_shouldReturnUserDtoList() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));

        List<UserDto> result = userService.findAll();

        assertFalse(result.isEmpty());
        assertEquals("username", result.get(0).getName());
    }

    @Test
    void save_shouldThrowExceptionIfUsernameOrEmailExists() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(userEntity));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(userEntity));

        UserRequest newUser = new UserRequest();
        newUser.setUsername("username");
        newUser.setEmail("user@example.com");
        newUser.setPassword("pass");

        Exception ex = assertThrows(UserRegistrationException.class, () -> userService.save(newUser));
        assertTrue(ex.getMessage().contains("Error en el registro"));
    }

    @Test
    void save_shouldSaveUserWhenValid() throws Exception {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserRequest newUser = new UserRequest();
        newUser.setUsername("newuser");
        newUser.setEmail("new@example.com");
        newUser.setPassword("pass123");

        UserDto savedUser = userService.save(newUser);

        assertNotNull(savedUser);
        assertEquals(1L, savedUser.getId());
        assertEquals("newuser", savedUser.getName());
    }

    @Test
    void findById_shouldReturnUserDto_whenUserExists() throws ResourceNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        Optional<UserDto> userOpt = userService.findById(1L);

        assertTrue(userOpt.isPresent());
        assertEquals("username", userOpt.get().getName());
    }

    @Test
    void findById_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void update_shouldUpdateUser_whenValid() throws Exception {
        UserRequest updateRequest = new UserRequest();
        updateRequest.setUsername("updatedUser");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setPassword("newpass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.findByUsername("updatedUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("updated@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserDto updated = userService.update(1L, updateRequest);

        assertEquals("updatedUser", updated.getName());
        assertEquals("updated@example.com", updated.getEmail());
    }

    @Test
    void update_shouldThrowWhenUsernameTaken() {
        UserRequest updateRequest = new UserRequest();
        updateRequest.setUsername("takenUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.findByUsername("takenUser")).thenReturn(Optional.of(new User()));

        assertThrows(UsernameTakenException.class, () -> userService.update(1L, updateRequest));
    }

    @Test
    void delete_shouldDeleteUser_whenExists() throws ResourceNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository).deleteById(1L);

        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_shouldThrow_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.delete(1L));
    }

    @Test
    void findByUsername_shouldReturnUserDto_whenFound() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(userEntity));

        Optional<UserDto> result = userService.findByUsername("username");

        assertTrue(result.isPresent());
        assertEquals("username", result.get().getName());
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenNotFound() {
        when(userRepository.findByUsername("not_found")).thenReturn(Optional.empty());

        Optional<UserDto> result = userService.findByUsername("not_found");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByEmail_shouldReturnUserDto_whenFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(userEntity));

        Optional<UserDto> result = userService.findByEmail("user@example.com");

        assertTrue(result.isPresent());
        assertEquals("user@example.com", result.get().getEmail());
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenNotFound() {
        when(userRepository.findByEmail("not_found@example.com")).thenReturn(Optional.empty());

        Optional<UserDto> result = userService.findByEmail("not_found@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void exportUsersToExcel_shouldReturnValidExcelFile() throws Exception {
        UserDto user1 = new UserDto();
        user1.setId(1L);
        user1.setName("testuser1");
        user1.setEmail("test1@example.com");

        UserDto user2 = new UserDto();
        user2.setId(2L);
        user2.setName("testuser2");
        user2.setEmail("test2@example.com");

        // Mockeamos findAll para devolver los dos usuarios DTO
        when(userRepository.findAll()).thenReturn(List.of(
                UserMapper.mapper.toEntity(user1),
                UserMapper.mapper.toEntity(user2)
        ));

        byte[] excelData = userService.exportUsersToExcel();

        assertNotNull(excelData);
        assertTrue(excelData.length > 0);

        // Verifica que se puede leer el archivo Excel generado
        try (ByteArrayInputStream input = new ByteArrayInputStream(excelData);
             XSSFWorkbook workbook = new XSSFWorkbook(input)) {

            XSSFSheet sheet = workbook.getSheet("Users");
            assertNotNull(sheet);

            // Header y dos filas de datos
            assertEquals(3, sheet.getPhysicalNumberOfRows());
            assertEquals("ID", sheet.getRow(0).getCell(0).getStringCellValue());
        }
    }

}