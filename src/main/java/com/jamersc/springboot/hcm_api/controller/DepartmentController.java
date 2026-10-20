package com.jamersc.springboot.hcm_api.controller;

import com.jamersc.springboot.hcm_api.dto.department.DepartmentCreateDto;
import com.jamersc.springboot.hcm_api.dto.department.DepartmentPatchDto;
import com.jamersc.springboot.hcm_api.dto.department.DepartmentResponseDto;
import com.jamersc.springboot.hcm_api.service.department.DepartmentService;
import com.jamersc.springboot.hcm_api.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PreAuthorize("hasAuthority('VIEW_DEPARTMENTS')")
    @GetMapping("/")
    public ResponseEntity<ApiResponse<Page<DepartmentResponseDto>>> getAllDepartments(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<DepartmentResponseDto> retrievedDepartments = departmentService.getAllDepartments(
                search, dateFrom, dateTo, pageable
        );
        return ResponseEntity.ok(ApiResponse.<Page<DepartmentResponseDto>>builder()
                .success(true)
                .message("List of departments retrieved successfully!")
                .data(retrievedDepartments)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('CREATE_DEPARTMENTS')")
    @PostMapping("/")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> createDepartment(
            @Valid @RequestBody DepartmentCreateDto createDto,
            Authentication authentication) {
        DepartmentResponseDto createdDepartment = departmentService.createDepartment(createDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<DepartmentResponseDto>builder()
                .success(true)
                .message("Department created successfully!")
                .data(createdDepartment)
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('VIEW_DEPARTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<DepartmentResponseDto>>> getDepartment(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<Optional<DepartmentResponseDto>>builder()
                .success(true)
                .message("Department retrieved successfully!")
                .data(departmentService.getDepartment(id))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('UPDATE_DEPARTMENTS')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> updateDepartment(
            @PathVariable String id,
            @RequestBody DepartmentPatchDto patchDTO,
            Authentication authentication) {
        DepartmentResponseDto updatedDepartment = departmentService.updateDepartment(id, patchDTO, authentication);
        return ResponseEntity.ok(ApiResponse.<DepartmentResponseDto>builder()
                .success(true)
                .message("Department updated successfully!")
                .data(updatedDepartment)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('ARCHIVE_DEPARTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> archiveDepartment(
            @PathVariable String id,
            Authentication authentication) {
        departmentService.archiveDepartment(id, authentication);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Department archived successfully!")
                .data(null)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('UNARCHIVED_DEPARTMENTS')")
    @PatchMapping("/{id}/unarchived")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> unarchivedDepartment(
            @PathVariable String id,
            Authentication authentication) {
        DepartmentResponseDto unarchivedDepartment = departmentService.unarchivedDepartment(id, authentication);
        return ResponseEntity.ok(ApiResponse.<DepartmentResponseDto>builder()
                .success(true)
                .message("Department unarchived successfully!")
                .data(unarchivedDepartment)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }
}
