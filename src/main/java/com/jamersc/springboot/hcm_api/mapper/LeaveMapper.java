package com.jamersc.springboot.hcm_api.mapper;

import com.jamersc.springboot.hcm_api.dto.leave.LeaveCreateDto;
import com.jamersc.springboot.hcm_api.dto.leave.LeaveDto;
import com.jamersc.springboot.hcm_api.dto.leave.LeaveResponseDto;
import com.jamersc.springboot.hcm_api.entity.Leave;
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
public abstract class LeaveMapper {

    @Autowired
    protected HashidService hashService;

    @Mapping(target = "id", expression = "java(hashService.encode(leave.getId()))")
    @Mapping(target = "approvedBy", source = "approvedBy.employee.job.title")
    @Mapping(target = "employeeFullName", source = "employee.employeeFullName")
    public abstract LeaveResponseDto entityToResponseDto(Leave leave);

    public abstract LeaveDto entityToDto(Leave leave);

    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", expression = "java(hashService.decode(dto.getId()))")
    public abstract Leave dtoToEntity(LeaveDto dto);

    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", expression = "java(hashService.decode(responseDTO.getId()))")
    public abstract Leave responseDtoToEntity(LeaveResponseDto responseDTO);

    public abstract LeaveCreateDto entityToCreateDto(Leave leave);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    public abstract Leave createDtoToEntity(LeaveCreateDto dto);

    public abstract List<LeaveResponseDto> entitiesToResponseDtos(List<Leave> leaves);

}
