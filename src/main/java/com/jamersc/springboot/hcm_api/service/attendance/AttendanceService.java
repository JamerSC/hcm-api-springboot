package com.jamersc.springboot.hcm_api.service.attendance;

import com.jamersc.springboot.hcm_api.dto.attendance.AttendanceDto;
import com.jamersc.springboot.hcm_api.dto.attendance.AttendanceResponseDto;
import com.jamersc.springboot.hcm_api.entity.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceService {

    Page<AttendanceDto> getAllAttendances(
            String search,
            AttendanceStatus status,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    );

    Page<AttendanceResponseDto> getMyAttendances(
            String search,
            AttendanceStatus status,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable,
            Authentication authentication
    );

    Optional<AttendanceResponseDto> getAttendance(String id); // Hash ID lookup

    AttendanceResponseDto checkIn(Authentication authentication);

    AttendanceResponseDto checkOut(Authentication authentication);
}
