package com.jamersc.springboot.hcm_api.mapper;

import com.jamersc.springboot.hcm_api.dto.applicant.ApplicantDto;
import com.jamersc.springboot.hcm_api.dto.applicant.ApplicantProfileDto;
import com.jamersc.springboot.hcm_api.dto.applicant.ApplicantResponseDto;
import com.jamersc.springboot.hcm_api.entity.Applicant;
import com.jamersc.springboot.hcm_api.service.security.HashidService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {HashidService.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class ApplicantMapper {

    @Autowired
    protected HashidService hashService;

    @Mapping(target = "id", expression = "java(hashService.encode(applicant.getId()))")
    public abstract ApplicantDto entityToApplicantDto(Applicant applicant);

    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract Applicant applicantDtoToEntity(ApplicantDto dto);

    @Mapping(target = "id", expression = "java(hashService.encode(applicant.getId()))")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "applicantFullName", source = "applicantFullName")
    public abstract ApplicantResponseDto entityToResponseDto(Applicant applicant);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "educationLevel", ignore = true)
    @Mapping(target = "cvFilePath", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "address", ignore = true)
    public abstract Applicant responseDtoToEntity(ApplicantResponseDto dto);

//    @Mapping(target = "id", expression = "java(hashService.encode(applicant.getId()))")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "username", source = "user.username")
    public abstract ApplicantProfileDto entityToProfileDto(Applicant applicant);

    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract Applicant profileDtoToEntity(ApplicantDto dto);

    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cvFilePath", ignore = true)
    public abstract void updateEntityFromProfileDto(ApplicantProfileDto dto, @MappingTarget Applicant applicant);

    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cvFilePath", ignore = true)
    public abstract Applicant profileDtoToNewEntity(ApplicantProfileDto dto);

    public abstract List<ApplicantDto> entityToApplicantDtoList(List<Applicant> applicants);
    public abstract List<ApplicantResponseDto> entitiesToResponseDtos(List<Applicant> applicant);
    public abstract List<Applicant> responseDtosToEntities(List<ApplicantResponseDto> dto);
}