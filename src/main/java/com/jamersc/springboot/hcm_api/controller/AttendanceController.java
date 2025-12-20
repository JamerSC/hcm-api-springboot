package com.jamersc.springboot.hcm_api.controller;

import com.jamersc.springboot.hcm_api.dto.attendance.AttendanceDto;
import com.jamersc.springboot.hcm_api.dto.attendance.AttendanceResponseDto;
import com.jamersc.springboot.hcm_api.dto.job.JobResponseDto;
import com.jamersc.springboot.hcm_api.service.attendance.AttendanceService;
import com.jamersc.springboot.hcm_api.service.employee.EmployeeService;
import com.jamersc.springboot.hcm_api.utils.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService, EmployeeService employeeService) {
        this.attendanceService = attendanceService;
    }

    @PreAuthorize("hasAuthority('VIEW_ATTENDANCES')")
    @GetMapping("/")
    public ResponseEntity<ApiResponse<Page<AttendanceDto>>> getAllAttendances(
            @PageableDefault(page = 0, size = 10, sort = "attendanceDate") Pageable pageable) {
        Page<AttendanceDto> retrievedAttendances = attendanceService.getAllAttendance(pageable);
        ApiResponse<Page<AttendanceDto>> response = ApiResponse.<Page<AttendanceDto>>builder()
                .success(true)
                .message("List of attendances retrieved successfully!")
                .data(retrievedAttendances)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VIEW_MY_ATTENDANCES')")
    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<Page<AttendanceResponseDto>>> myAttendances(
            @PageableDefault(page = 0, size = 10, sort = "attendanceDate") Pageable pageable,
            Authentication authentication)
    {
        Page<AttendanceResponseDto> retrievedMyAttendances = attendanceService
                .getMyAttendances(pageable, authentication);
        ApiResponse<Page<AttendanceResponseDto>> response = ApiResponse.<Page<AttendanceResponseDto>>builder()
                .success(true)
                .message("List of my attendances retrieved successfully!")
                .data(retrievedMyAttendances)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CHECK_IN_ATTENDANCES')")
    @PostMapping("/check-in")
    public ResponseEntity<ApiResponse<AttendanceResponseDto>> attendanceCheckIn(Authentication authentication) {
        AttendanceResponseDto checkedInAttendance = attendanceService
                .checkIn(authentication);
        ApiResponse<AttendanceResponseDto> response = ApiResponse.<AttendanceResponseDto>builder()
                .success(true)
                .message("Attendance checked in successfully!")
                .data(checkedInAttendance)
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CHECK_OUT_ATTENDANCES')")
    @PatchMapping("/check-out")
    public ResponseEntity<ApiResponse<AttendanceResponseDto>> attendanceCheckOut(Authentication authentication) {
        AttendanceResponseDto checkedOutAttendance = attendanceService
                .checkOut(authentication);
        ApiResponse<AttendanceResponseDto> response = ApiResponse.<AttendanceResponseDto>builder()
                .success(true)
                .message("Attendance checked out successfully!")
                .data(checkedOutAttendance)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
