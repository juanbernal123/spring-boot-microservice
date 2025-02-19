package com.example.SpringBootMicroservice.mapper;

import com.example.SpringBootMicroservice.dto.request.RoleRequest;
import com.example.SpringBootMicroservice.dto.response.RoleDto;
import com.example.SpringBootMicroservice.entity.Role;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface RoleMapper {
    RoleMapper mapper = Mappers.getMapper(RoleMapper.class);

    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    RoleDto toDto(Role role);

    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    Role toEntity(RoleDto roleDto);

    Role requestToDto(RoleRequest roleRequest);

    List<RoleDto> toDtoList(List<Role> roles);
    List<Role> toEntityList(List<RoleDto> roleDtos);
}
