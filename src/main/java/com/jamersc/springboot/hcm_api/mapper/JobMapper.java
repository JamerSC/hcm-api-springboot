package com.jamersc.springboot.hcm_api.mapper;

import com.jamersc.springboot.hcm_api.dto.job.JobCreateDto;
import com.jamersc.springboot.hcm_api.dto.job.JobDto;
import com.jamersc.springboot.hcm_api.dto.job.JobPatchDto;
import com.jamersc.springboot.hcm_api.dto.job.JobResponseDto;
import com.jamersc.springboot.hcm_api.entity.Job;
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
public abstract class JobMapper {

    @Autowired
    protected HashidService hashService;

    @Mapping(target = "id", expression = "java(hashService.encode(job.getId()))")
    @Mapping(target = "department", source = "department.name")
    @Mapping(target = "updatedBy", source = "updatedBy.employee.job.title")
    @Mapping(target = "createdBy", source = "createdBy.employee.job.title")
    public abstract JobDto entityToJobDto(Job job);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "postedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract Job jobDtoToEntity(JobDto dto);

    @Mapping(target = "departmentId", expression = "java(hashService.encode(job.getDepartment() != null ? job.getDepartment().getId() : null))")
    public abstract JobCreateDto entityToJobCreateDto(Job job);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "postedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract Job jobCreateDtoToEntity(JobCreateDto dto);

    @Mapping(target = "departmentId", expression = "java(hashService.encode(job.getDepartment() != null ? job.getDepartment().getId() : null))")
    public abstract JobPatchDto entityToPatchDto(Job job);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "postedDate", ignore = true)
    @Mapping(target = "postedBy", ignore = true)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract Job patchDtoToEntity(JobPatchDto dto);

    @Mapping(target = "jobId", expression = "java(hashService.encode(job.getId()))")
    @Mapping(target = "postedBy", source = "postedBy.employee.job.title")
    @Mapping(target = "department", source = "department.name")
    public abstract JobResponseDto entityToJobResponseDto(Job job);

    public abstract List<JobDto> entitiesToJobDtos(List<Job> jobs);
    public abstract List<Job> jobDtosToEntities(List<JobDto> dtos);
}