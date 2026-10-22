package com.jamersc.springboot.hcm_api.mapper;

import com.jamersc.springboot.hcm_api.dto.attendance.AttendanceDto;
import com.jamersc.springboot.hcm_api.dto.attendance.AttendanceResponseDto;
import com.jamersc.springboot.hcm_api.entity.Attendance;
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
public abstract class AttendanceMapper {

    @Autowired
    protected HashidService hashService;

    @Mapping(target = "attendanceId", expression = "java(hashService.encode(attendance.getId()))")
    @Mapping(target = "employeeId", expression = "java(hashService.encode(attendance.getEmployee() != null ? attendance.getEmployee().getId() : null))")
    @Mapping(target = "employeeName", source = "employee.employeeFullName")
    @Mapping(target = "jobTitle", source = "employee.job.title")
    @Mapping(target = "department", source = "employee.job.department.name")
    @Mapping(target = "createdBy", source = "createdBy.employee.job.title")
    @Mapping(target = "updatedBy", source = "updatedBy.employee.job.title")
    public abstract AttendanceDto entityToDto(Attendance attendance);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "attendanceDate", ignore = true)
    @Mapping(target = "checkInTime", ignore = true)
    @Mapping(target = "checkOutTime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Attendance dtoToEntity(AttendanceDto dto);

    @Mapping(target = "attendanceId", expression = "java(hashService.encode(attendance.getId()))")
    @Mapping(target = "employeeId", expression = "java(hashService.encode(attendance.getEmployee() != null ? attendance.getEmployee().getId() : null))")
    @Mapping(target = "employeeName", source = "employee.employeeFullName")
    @Mapping(target = "jobTitle", source = "employee.job.title")
    @Mapping(target = "department", source = "employee.job.department.name")
    public abstract AttendanceResponseDto entityToResponseDto(Attendance attendance);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "attendanceDate", ignore = true)
    @Mapping(target = "checkInTime", ignore = true)
    @Mapping(target = "checkOutTime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Attendance responseDtoToEntity(AttendanceResponseDto dto);

    public abstract List<AttendanceDto> entitiesToDtos(List<Attendance> attendances);

    public abstract List<AttendanceResponseDto> entitiesToResponseDtos(List<Attendance> attendances);
}
