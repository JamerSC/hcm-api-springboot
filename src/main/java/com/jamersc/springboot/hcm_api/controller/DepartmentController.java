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
    private ResponseEntity<ApiResponse<Page<DepartmentResponseDto>>> getAllDepartments(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<DepartmentResponseDto> retrievedDepartments = departmentService.getAllDepartments(pageable);
        ApiResponse<Page<DepartmentResponseDto>> response = ApiResponse.<Page<DepartmentResponseDto>>builder()
                .success(true)
                .message("List of departments retrieved successfully!")
                .data(retrievedDepartments)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CREATE_DEPARTMENTS')")
    @PostMapping("/")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> createDepartment(
            @Valid @RequestBody DepartmentCreateDto createDto,
            Authentication authentication) {
        DepartmentResponseDto createdDepartment = departmentService.createDepartment(createDto, authentication);
        ApiResponse<DepartmentResponseDto> response = ApiResponse.<DepartmentResponseDto>builder()
                .success(true)
                .message("Department created successfully!")
                .data(createdDepartment)
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VIEW_DEPARTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<DepartmentResponseDto>>> getDepartment(@PathVariable Long id) {
        Optional<DepartmentResponseDto> retrievedDepartment = departmentService.getDepartment(id);
        ApiResponse<Optional<DepartmentResponseDto>> response = ApiResponse.<Optional<DepartmentResponseDto>>builder()
                .success(true)
                .message("Department retrieved successfully!")
                .data(retrievedDepartment)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('UPDATE_DEPARTMENTS')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> updateDepartment(
            @PathVariable Long id, @RequestBody DepartmentPatchDto patchDTO, Authentication authentication) {
        DepartmentResponseDto updatedDepartment = departmentService.updateDepartment(id, patchDTO, authentication);
        ApiResponse<DepartmentResponseDto> response = ApiResponse.<DepartmentResponseDto>builder()
                .success(true)
                .message("Department updated successfully!")
                .data(updatedDepartment)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ARCHIVE_DEPARTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> archiveDepartment(
            @PathVariable Long id, Authentication authentication) {
        Optional<DepartmentResponseDto> department =
                departmentService.getDepartment(id);

        if (department.isEmpty()) {
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .success(true)
                    .message("Department not found!")
                    .data(null)
                    .status(HttpStatus.NOT_FOUND.value())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(response);
        }

        departmentService.archiveDepartment(id, authentication);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Department archived successfully!")
                .data(null)
                .status(HttpStatus.NO_CONTENT.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('UNARCHIVED_DEPARTMENTS')")
    @PatchMapping("/{id}/unarchived")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> unarchivedDepartment(
            @PathVariable Long id, Authentication authentication) {
        DepartmentResponseDto unarchivedDepartment = departmentService
                .unarchivedDepartment(id, authentication);
        ApiResponse<DepartmentResponseDto> response = ApiResponse.<DepartmentResponseDto>builder()
                .success(true)
                .message("Department unarchived successfully!")
                .data(unarchivedDepartment)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
