package com.example.SpringBootMicroservice.repository;

import com.example.SpringBootMicroservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
}
