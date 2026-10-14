package com.jamersc.springboot.hcm_api.mapper;

import com.jamersc.springboot.hcm_api.dto.department.DepartmentCreateDto;
import com.jamersc.springboot.hcm_api.dto.department.DepartmentDto;
import com.jamersc.springboot.hcm_api.dto.department.DepartmentPatchDto;
import com.jamersc.springboot.hcm_api.dto.department.DepartmentResponseDto;
import com.jamersc.springboot.hcm_api.entity.Department;
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
public abstract class DepartmentMapper {

    @Autowired
    protected HashidService hashService;

    @Mapping(target = "id", expression = "java(hashService.encode(dept.getId()))")
    public abstract DepartmentDto entityToDeptDto(Department dept);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "jobs", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract Department deptDtoToEntity(DepartmentDto dto);

    public abstract DepartmentCreateDto entityToCreateDto(Department dept);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "jobs", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract Department createDtoToEntity(DepartmentCreateDto dto);

    @Mapping(target = "id", expression = "java(hashService.encode(dept.getId()))")
    @Mapping(target = "updatedBy", source = "updatedBy.employee.job.title")
    @Mapping(target = "createdBy", source = "createdBy.employee.job.title")
    public abstract DepartmentResponseDto entityToDepartmentResponseDto(Department dept);

    public abstract DepartmentPatchDto entityToPatchDto(Department dept);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "jobs", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract Department patchDtoToEntity(DepartmentPatchDto dto);

    public abstract List<DepartmentDto> entitiesToDeptDtos(List<Department> departments);
    public abstract List<DepartmentResponseDto> entitiesToDeptResponseDto(List<Department> departments);
    public abstract List<Department> deptDtosToEntities(List<DepartmentDto> deptDtos);
}