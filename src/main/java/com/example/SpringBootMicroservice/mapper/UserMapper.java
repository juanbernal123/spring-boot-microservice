package com.example.SpringBootMicroservice.mapper;

import com.example.SpringBootMicroservice.dto.request.UserRequest;
import com.example.SpringBootMicroservice.dto.response.UserDto;
import com.example.SpringBootMicroservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface UserMapper {

    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "username", target = "name")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    UserDto toDto(User user);

    @Mapping(source = "name", target = "username")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    User toEntity(UserDto userDto);

    User requestToEntity(UserRequest userRequest);

    List<UserDto> toDtoList(List<User> users);
    List<User> toEntityList(List<UserDto> userDtos);
}
