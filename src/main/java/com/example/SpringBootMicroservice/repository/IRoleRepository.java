package com.example.SpringBootMicroservice.repository;

import com.example.SpringBootMicroservice.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @EntityGraph(attributePaths = {"permissions"})
    Optional<Role> findWithPermissionsById(Long id);
}
