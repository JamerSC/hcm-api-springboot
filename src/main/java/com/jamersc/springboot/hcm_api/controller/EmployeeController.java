package com.jamersc.springboot.hcm_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jamersc.springboot.hcm_api.dto.employee.*;
import com.jamersc.springboot.hcm_api.entity.LeaveStatus;
import com.jamersc.springboot.hcm_api.entity.LeaveType;
import com.jamersc.springboot.hcm_api.service.employee.EmployeeService;
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
import java.util.*;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;

    public EmployeeController(EmployeeService employeeService, ObjectMapper objectMapper) {
        this.employeeService = employeeService;
        this.objectMapper = objectMapper;
    }


    @PreAuthorize("hasAuthority('VIEW_EMPLOYEES')")
    @GetMapping("/")
    public ResponseEntity<ApiResponse<Page<EmployeeResponseDto>>> getAllEmployees(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<EmployeeResponseDto> retrievedEmployees = employeeService.getAllEmployee(pageable);
        ApiResponse<Page<EmployeeResponseDto>> response = ApiResponse.<Page<EmployeeResponseDto>>builder()
                .success(true)
                .message("List of employees retrieved successfully!")
                .data(retrievedEmployees)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VIEW_EMPLOYEES')")
    // Get employee profile with username & role
    @GetMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<Optional<EmployeeProfileDto>>> getEmployeeProfile(@PathVariable Long id) {
        Optional<EmployeeProfileDto> retrievedEmployeeProfile= employeeService.getEmployeeProfile(id);
        ApiResponse<Optional<EmployeeProfileDto>> response = ApiResponse.<Optional<EmployeeProfileDto>>builder()
                .success(true)
                .message("Employee profile retrieved successfully!")
                .data(retrievedEmployeeProfile)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VIEW_EMPLOYEES')")
    // Get employee by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<EmployeeResponseDto>>> getEmployee(@PathVariable Long id) {
        Optional<EmployeeResponseDto> retrievedEmployee = employeeService.getEmployee(id);
        ApiResponse<Optional<EmployeeResponseDto>> response = ApiResponse.<Optional<EmployeeResponseDto>>builder()
                .success(true)
                .message("Employee retrieved successfully!")
                .data(retrievedEmployee)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VIEW_PROFILE')")
    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<EmployeeProfileDto>> myEmployeeProfile(Authentication authentication) {
        EmployeeProfileDto retrieveMyProfile = employeeService.getMyEmployeeProfile(authentication);
        ApiResponse<EmployeeProfileDto> response = ApiResponse.<EmployeeProfileDto>builder()
                .success(true)
                .message("My employee profile retrieved successfully!")
                .data(retrieveMyProfile)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CREATE_EMPLOYEES')")
    @PostMapping("/")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> createEmployee(
            @Valid @RequestBody EmployeeCreateDto dto, Authentication authentication) {
        EmployeeResponseDto createdEmployee = employeeService.createEmployee(dto, authentication);
        ApiResponse<EmployeeResponseDto> response = ApiResponse.<EmployeeResponseDto>builder()
                .success(true)
                .message("Employee created successfully!")
                .data(createdEmployee)
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('UPDATE_EMPLOYEES')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> updateEmployee(
            @PathVariable Long id, @RequestBody EmployeePatchDto dto, Authentication authentication) {
        EmployeeResponseDto patchedEmployee = employeeService.patchEmployee(id, dto, authentication);
        ApiResponse<EmployeeResponseDto> response = ApiResponse.<EmployeeResponseDto>builder()
                .success(true)
                .message("Employee updated successfully!")
                .data(patchedEmployee)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    private EmployeeDto apply(Map<String, Object> patchPayload, EmployeeDto tempEmployee) {
        // convert employee object to JSON object node
        // add Object mapper in dependency injection
        ObjectNode employeeNode = objectMapper.convertValue(tempEmployee, ObjectNode.class);

        // convert the patchPayload map to a JSON object node
        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);

        // merge patch updates into the employee node
        employeeNode.setAll(patchNode);

        // return - convert JSON object node back to Employee Object
        return objectMapper.convertValue(employeeNode, EmployeeDto.class);
    }

    // todo improve validation
    @PreAuthorize("hasAuthority('ARCHIVE_EMPLOYEES')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> archiveEmployee(
            @PathVariable Long id, Authentication authentication) {
        Optional<EmployeeDto> tempEmployee = employeeService.findById(id);

        if (tempEmployee.isEmpty()) {
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .success(true)
                    .message("Employee not found!")
                    .data(null)
                    .status(HttpStatus.NOT_FOUND.value())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(response);
        }

        employeeService.archiveEmployee(id, authentication);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Employee archived successfully!")
                .data(null)
                .status(HttpStatus.NO_CONTENT.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    // todo improve validation
    @PreAuthorize("hasAuthority('UNARCHIVED_EMPLOYEES')")
    @PatchMapping("/{id}/unarchived")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> unarchivedEmployee(
            @PathVariable Long id, Authentication authentication) {
        EmployeeResponseDto unarchivedEmployee = employeeService.unarchivedEmployee(id, authentication);
        ApiResponse<EmployeeResponseDto> response = ApiResponse.<EmployeeResponseDto>builder()
                .success(true)
                .message("Employee unarchived successfully!")
                .data(unarchivedEmployee)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
