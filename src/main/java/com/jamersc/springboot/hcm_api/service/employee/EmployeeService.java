package com.jamersc.springboot.hcm_api.service.employee;

import com.jamersc.springboot.hcm_api.dto.employee.*;
import com.jamersc.springboot.hcm_api.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Optional;

public interface EmployeeService {
    Page<EmployeeResponseDto> getAllEmployees(
            String search,
            String jobId,          // Hash ID
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    );

    Optional<EmployeeProfileDto> getEmployeeProfile(String id);
    Optional<EmployeeResponseDto> getEmployee(String id);
    Optional<EmployeeDto> findByEmployee(String id);
    EmployeeProfileDto getMyEmployeeProfile(Authentication authentication);
    EmployeeResponseDto createEmployee(EmployeeCreateDto dto, Authentication authentication);
    EmployeeResponseDto patchEmployee(String id, EmployeePatchDto dto, Authentication authentication);
    void archiveEmployee(String id, Authentication authentication);
    EmployeeResponseDto unarchivedEmployee(String id, Authentication authentication);
}
