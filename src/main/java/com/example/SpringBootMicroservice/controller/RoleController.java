package com.example.SpringBootMicroservice.controller;

import com.example.SpringBootMicroservice.service.IUserService;
import com.example.SpringBootMicroservice.service.impl.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;
}
