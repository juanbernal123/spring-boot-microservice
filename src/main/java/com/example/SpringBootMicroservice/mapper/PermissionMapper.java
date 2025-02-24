package com.example.SpringBootMicroservice.mapper;

import com.example.SpringBootMicroservice.dto.request.PermissionRequest;
import com.example.SpringBootMicroservice.dto.response.PermissionDto;
import com.example.SpringBootMicroservice.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PermissionMapper {

    PermissionMapper mapper = Mappers.getMapper(PermissionMapper.class);

    @Mapping(source = "displayName", target = "display_name")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    PermissionDto toDto(Permission permission);

    @Mapping(source = "display_name", target = "displayName")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    Permission toEntity(PermissionDto dto);

    @Mapping(source = "display_name", target = "displayName")
    Permission requestEntity(PermissionRequest request);

    List<PermissionDto> toDtoList(List<Permission> permissions);
    List<Permission> toEntityList(List<PermissionDto> permissionDtos);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "display_name", target = "displayName")
    void updateEntityFromRequest(PermissionRequest request, @MappingTarget Permission permission);
}
