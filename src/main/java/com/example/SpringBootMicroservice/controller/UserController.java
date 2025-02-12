package com.example.SpringBootMicroservice.controller;

import com.example.SpringBootMicroservice.dto.request.UserRequest;
import com.example.SpringBootMicroservice.dto.response.UserDto;
import com.example.SpringBootMicroservice.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserRequest request) throws Exception {
        return ResponseEntity.ok(userService.save(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) throws Exception {
        Optional<UserDto> findUser = userService.findById(id);
        return findUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserRequest request) throws Exception {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        userService.delete(id);
        return ResponseEntity.ok("Se elimino el registro con id: " +id);
    }
}
