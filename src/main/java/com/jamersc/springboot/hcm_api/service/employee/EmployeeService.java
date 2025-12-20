package com.jamersc.springboot.hcm_api.service.employee;

import com.jamersc.springboot.hcm_api.dto.employee.*;
import com.jamersc.springboot.hcm_api.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface EmployeeService {
    Page<EmployeeResponseDto> getAllEmployee(Pageable pageable);
    //EmployeeDto findById(Long id);
    Optional<EmployeeProfileDto> getEmployeeProfile(Long id);
    Optional<EmployeeResponseDto> getEmployee(Long id);
    Optional<EmployeeDto> findById(Long id);
    EmployeeProfileDto getMyEmployeeProfile(Authentication authentication);
    EmployeeResponseDto createEmployee(EmployeeCreateDto dto, Authentication authentication);
    Employee updateEmployee(EmployeeUpdateDto dto, Authentication authentication);
    EmployeeResponseDto patchEmployee(Long id, EmployeePatchDto dto, Authentication authentication);
    void archiveEmployee(Long id, Authentication authentication);
    EmployeeResponseDto unarchivedEmployee(Long id, Authentication authentication);
}
