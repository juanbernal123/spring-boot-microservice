package com.example.SpringBootMicroservice.controller;

import com.example.SpringBootMicroservice.dto.request.AssignPermissionsRequest;
import com.example.SpringBootMicroservice.dto.request.RoleRequest;
import com.example.SpringBootMicroservice.dto.response.RoleDetailDto;
import com.example.SpringBootMicroservice.dto.response.RoleDto;
import com.example.SpringBootMicroservice.service.impl.RoleService;
import com.example.SpringBootMicroservice.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private ResponseUtil responseUtil;

    @GetMapping
    public ResponseEntity<List<RoleDto>> findAll(HttpServletRequest request) {
        return responseUtil.success(roleService.findAll(), request);
    }

    @PostMapping
    public ResponseEntity<RoleDto> create(@Valid @RequestBody RoleRequest roleRequest, HttpServletRequest request) throws Exception {
        return responseUtil.success(roleService.save(roleRequest), request, "created.successfully.message", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDetailDto> findById(@PathVariable Long id, HttpServletRequest request) throws Exception {
        Optional<RoleDetailDto> findRole = roleService.findById(id);
        return findRole.map(role -> responseUtil.success(role, request))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> update(@PathVariable Long id, @RequestBody RoleRequest roleRequest, HttpServletRequest request) throws Exception {
        return responseUtil.success(roleService.update(id, roleRequest), request, "updated.successfully.message", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RoleDto> delete(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return responseUtil.success(roleService.delete(id), request, HttpStatus.OK , "deleted.successfully.message", id);
    }

    @PutMapping("/{id}/permissions")
    public ResponseEntity<?> assignPermissions(@PathVariable Long id, @Valid @RequestBody AssignPermissionsRequest permissionRequest, HttpServletRequest request) throws Exception {
        return responseUtil.success(roleService.assignPermissionToRole(id, permissionRequest.getPermissions()), request, HttpStatus.OK,"created.successfully.message");
    }
}
