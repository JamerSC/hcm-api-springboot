package com.jamersc.springboot.hcm_api.mapper;

import com.jamersc.springboot.hcm_api.dto.application.ApplicationDto;
import com.jamersc.springboot.hcm_api.dto.application.ApplicationResponseDto;
import com.jamersc.springboot.hcm_api.dto.application.ApplicationUpdateDto;
import com.jamersc.springboot.hcm_api.entity.Application;
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
public abstract class ApplicationMapper {

    @Autowired
    protected HashidService hashService;

    @Mapping(target = "id", expression = "java(hashService.encode(application.getId()))")
    @Mapping(target = "jobDescription", source = "job.description")
    @Mapping(target = "createdBy", source = "createdBy.username")
    @Mapping(target = "updatedBy", source = "updatedBy.username")
    @Mapping(target = "jobPosition", source = "job.title")
    @Mapping(target = "applicantName", source = "applicant.firstName")
    public abstract ApplicationDto entityToDto(Application application);

    @Mapping(target = "source", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "proposedSalary", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "mobile", ignore = true)
    @Mapping(target = "linkedInProfile", ignore = true)
    @Mapping(target = "expectedSalary", ignore = true)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "degree", ignore = true)
    @Mapping(target = "availability", ignore = true)
    @Mapping(target = "applicationSummary", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "job", ignore = true)
    @Mapping(target = "applicant", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract Application dtoToEntity(ApplicationDto dto);

    @Mapping(target = "applicationId", expression = "java(hashService.encode(application.getId()))")
    @Mapping(target = "description", source = "job.description")
    @Mapping(target = "appliedPosition", source = "job.title")
    @Mapping(target = "applicantName", source = "applicant.applicantFullName")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract ApplicationUpdateDto entityToUpdateDto(Application application);

    @Mapping(target = "job", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicant", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    public abstract Application updateDtoToEntity(ApplicationUpdateDto dto);

    @Mapping(target = "applicationId", expression = "java(hashService.encode(application.getId()))")
    @Mapping(target = "description", source = "job.description")
    @Mapping(target = "appliedPosition", source = "job.title")
    @Mapping(target = "applicantName", source = "applicant.applicantFullName")
    public abstract ApplicationResponseDto entityToApplicationResponseDto(Application application);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "source", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "proposedSalary", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "mobile", ignore = true)
    @Mapping(target = "linkedInProfile", ignore = true)
    @Mapping(target = "expectedSalary", ignore = true)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "degree", ignore = true)
    @Mapping(target = "availability", ignore = true)
    @Mapping(target = "applicationSummary", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "job", ignore = true)
    @Mapping(target = "applicant", ignore = true)
    public abstract Application applicationResponseDtoToEntity(ApplicationResponseDto dto);

    public abstract List<ApplicationResponseDto> entitiesToResponseDtos(List<Application> applications);
    public abstract List<Application> responseDtosToEntities(List<ApplicationResponseDto> dtos);
}
