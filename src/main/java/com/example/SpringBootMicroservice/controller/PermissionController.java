package com.example.SpringBootMicroservice.controller;

import com.example.SpringBootMicroservice.dto.request.PermissionRequest;
import com.example.SpringBootMicroservice.dto.response.PermissionDto;
import com.example.SpringBootMicroservice.service.impl.PermissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<PermissionDto>> findAll() {
        return ResponseEntity.ok(permissionService.findAll());
    }

    @PostMapping
    public ResponseEntity<PermissionDto> create(@Valid @RequestBody PermissionRequest request) throws Exception {
        return ResponseEntity.ok(permissionService.save(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDto> findById(@PathVariable Long id) throws Exception {
        Optional<PermissionDto> findPermission = permissionService.findById(id);
        return findPermission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionDto> update(@PathVariable Long id, @Valid @RequestBody PermissionRequest request) throws Exception {
        return ResponseEntity.ok(permissionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        permissionService.delete(id);
        return ResponseEntity.ok("Se elimino el registro con id: " +id);
    }
}
