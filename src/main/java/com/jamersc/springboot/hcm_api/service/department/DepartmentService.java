package com.jamersc.springboot.hcm_api.service.department;

import com.jamersc.springboot.hcm_api.dto.department.DepartmentCreateDto;
import com.jamersc.springboot.hcm_api.dto.department.DepartmentPatchDto;
import com.jamersc.springboot.hcm_api.dto.department.DepartmentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Optional;

public interface DepartmentService {
    Page<DepartmentResponseDto> getAllDepartments(
            String search,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    );
    Optional<DepartmentResponseDto> getDepartment(String id);
    DepartmentResponseDto createDepartment(DepartmentCreateDto dto, Authentication authentication);
    DepartmentResponseDto updateDepartment(String id, DepartmentPatchDto dto, Authentication authentication);
    void archiveDepartment(String id, Authentication authentication);
    DepartmentResponseDto unarchivedDepartment(String id, Authentication authentication);
}
