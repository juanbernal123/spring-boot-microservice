package com.example.SpringBootMicroservice.controller;

import com.example.SpringBootMicroservice.dto.request.RoleRequest;
import com.example.SpringBootMicroservice.dto.response.RoleDto;
import com.example.SpringBootMicroservice.service.impl.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDto>> findAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @PostMapping
    public ResponseEntity<RoleDto> create(@Valid @RequestBody RoleRequest request) throws Exception {
        return ResponseEntity.ok(roleService.save(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> findById(@PathVariable Long id) throws Exception {
        Optional<RoleDto> findRole = roleService.findById(id);
        return findRole.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> update(@PathVariable Long id, @RequestBody RoleRequest request) throws Exception {
        return ResponseEntity.ok(roleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        roleService.delete(id);
        return ResponseEntity.ok("Se elimino el registro con id: " +id);
    }
}
