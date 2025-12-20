package com.jamersc.springboot.hcm_api.service.department;

import com.jamersc.springboot.hcm_api.dto.department.DepartmentCreateDto;
import com.jamersc.springboot.hcm_api.dto.department.DepartmentPatchDto;
import com.jamersc.springboot.hcm_api.dto.department.DepartmentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface DepartmentService {
    Page<DepartmentResponseDto> getAllDepartments(Pageable pageable);
    Optional<DepartmentResponseDto> getDepartment(Long id);
    DepartmentResponseDto createDepartment(DepartmentCreateDto dto, Authentication authentication);
    DepartmentResponseDto updateDepartment(Long id, DepartmentPatchDto dto, Authentication authentication);
    void archiveDepartment(Long id, Authentication authentication);
    DepartmentResponseDto unarchivedDepartment(Long id, Authentication authentication);
}
