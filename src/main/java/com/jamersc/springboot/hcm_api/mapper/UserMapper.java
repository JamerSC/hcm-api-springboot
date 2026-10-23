package com.jamersc.springboot.hcm_api.mapper;

import com.jamersc.springboot.hcm_api.dto.user.UserCreateDto;
import com.jamersc.springboot.hcm_api.dto.user.UserDto;
import com.jamersc.springboot.hcm_api.dto.user.UserResponseDto;
import com.jamersc.springboot.hcm_api.entity.User;
import com.jamersc.springboot.hcm_api.service.security.HashidService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {HashidService.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class UserMapper {

    @Autowired
    protected HashidService hashService;

    @Mapping(target = "id", expression = "java(hashService.encode(user.getId()))")
    public abstract UserDto entityToUserDTO(User user);

    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "applicant", ignore = true)
    public abstract User userDtoToEntity(UserDto dto);

    public abstract UserCreateDto entityToUserCreateDTO(User user);

    @Mapping(target = "active", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "applicant", ignore = true)
    public abstract User userCreateDtoToEntity(UserCreateDto dto);

    @Mapping(target = "id", expression = "java(hashService.encode(user.getId()))")
    @Mapping(target = "employeeId", expression = "java(hashService.encode(user.getEmployee() != null ? user.getEmployee().getId() : null))")
    @Mapping(target = "createdBy", source = "createdBy.employee.job.title")
    @Mapping(target = "updatedBy", source = "updatedBy.employee.job.title")
    public abstract UserResponseDto entityToUserResponseDTO(User user);

    @Mapping(target = "active", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "applicant", ignore = true)
    public abstract User userResponseDtoToEntity(UserResponseDto dto);

    public abstract List<UserDto> entitiesToDtos(List<User> users);

    public abstract List<User> dtosToEntities(List<UserDto> dtos);
}
