package com.jamersc.springboot.hcm_api.mapper;

import com.jamersc.springboot.hcm_api.dto.employee.*;
import com.jamersc.springboot.hcm_api.entity.Employee;
import com.jamersc.springboot.hcm_api.entity.Role;
import com.jamersc.springboot.hcm_api.service.security.HashidService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {HashidService.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class EmployeeMapper {

    @Autowired
    protected HashidService hashService;

    // Entity -> DTO (Encode Long to Hash)
    @Mapping(target = "id", expression = "java(hashService.encode(employee.getId()))")
    @Mapping(target = "jobId", expression = "java(hashService.encode(employee.getJob() != null ? employee.getJob().getId() : null))")
    public abstract EmployeeDto entityToDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "job", ignore = true)
    @Mapping(target = "leaves", ignore = true)
    @Mapping(target = "attendanceRecords", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    public abstract Employee dtoToEntity(EmployeeDto dto);

    // Profile DTO
    @Mapping(target = "id", expression = "java(hashService.encode(employee.getId()))")
    @Mapping(target = "jobPosition", source = "job.title")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "roles", expression = "java(mapRoles(employee.getUser() != null ? employee.getUser().getRoles() : null))")
    public abstract EmployeeProfileDto entityToProfileDto(Employee employee);

    // Response DTO
    @Mapping(target = "id", expression = "java(hashService.encode(employee.getId()))")
    @Mapping(target = "department", source = "job.department.name")
    @Mapping(target = "salary", expression = "java(employee.formattedSalary())")
    @Mapping(target = "jobPosition", source = "job.title")
    @Mapping(target = "updatedBy", source = "updatedBy.employee.job.title")
    @Mapping(target = "createdBy", source = "createdBy.employee.job.title")
    public abstract EmployeeResponseDto entityToEmployeeResponseDTO(Employee employee);

    // Create DTO -> Entity (Entity ID and Job are resolved in Service layer)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "job", ignore = true)
    @Mapping(target = "leaves", ignore = true)
    @Mapping(target = "attendanceRecords", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    public abstract Employee createDtoToEntity(EmployeeCreateDto dto);

    protected Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream().map(Role::getRoleName).collect(Collectors.toSet());
    }

    public abstract List<EmployeeDto> entitiesToDtos(List<Employee> employees);
    public abstract List<EmployeeResponseDto> entitiesToResponseDtos(List<Employee> employees);
}