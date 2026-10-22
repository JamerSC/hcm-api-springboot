package com.jamersc.springboot.hcm_api.service.attendance;

import com.jamersc.springboot.hcm_api.dto.attendance.AttendanceDto;
import com.jamersc.springboot.hcm_api.dto.attendance.AttendanceResponseDto;
import com.jamersc.springboot.hcm_api.entity.Attendance;
import com.jamersc.springboot.hcm_api.entity.AttendanceStatus;
import com.jamersc.springboot.hcm_api.entity.User;
import com.jamersc.springboot.hcm_api.mapper.AttendanceMapper;
import com.jamersc.springboot.hcm_api.repository.AttendanceRepository;
import com.jamersc.springboot.hcm_api.repository.AttendanceSpecification;
import com.jamersc.springboot.hcm_api.repository.EmployeeRepository;
import com.jamersc.springboot.hcm_api.repository.UserRepository;
import com.jamersc.springboot.hcm_api.service.security.HashidService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final AttendanceMapper attendanceMapper;
    private final HashidService hashService;

    @Override
    public Page<AttendanceDto> getAllAttendances(
            String search,
            AttendanceStatus status,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    ) {
        log.debug("Fetching all attendances filters: search={}, status={}, attendance date from {}, attendance date to {}, pageable={}",
                search, status, dateFrom, dateTo, pageable);

        Specification<Attendance> spec = Specification.allOf(
                AttendanceSpecification.search(search),
                AttendanceSpecification.hasStatus(status),
                AttendanceSpecification.dateRange(dateFrom, dateTo)
        );

        return attendanceRepository.findAll(spec, pageable)
                .map(attendanceMapper::entityToDto);
    }

    @Override
    public Page<AttendanceResponseDto> getMyAttendances(
            String search,
            AttendanceStatus status,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable,
            Authentication authentication
    ) {
        User currentUser = getUser(authentication);
        log.debug("Fetching my attendances filters: currentUser={}, search={}, status={}, attendance date from {}, attendance date to {}, pageable={}",
                currentUser.getEmployee(), search, status, dateFrom, dateTo, pageable);

        Specification<Attendance> spec = Specification.allOf(
                AttendanceSpecification.byEmployee(currentUser.getEmployee()),
                AttendanceSpecification.search(search),
                AttendanceSpecification.hasStatus(status),
                AttendanceSpecification.dateRange(dateFrom, dateTo)
        );

        return attendanceRepository.findAll(spec, pageable)
                .map(attendanceMapper::entityToResponseDto);
    }

    @Override
    public Optional<AttendanceResponseDto> getAttendance(String id) {
        Long rawId = hashService.decode(id);
        return Optional.of(attendanceRepository.findById(rawId)
                .map(attendanceMapper::entityToResponseDto)
                .orElseThrow(() -> new RuntimeException("Attendance record not found with id: " + id)));
    }

    @Override
    public AttendanceResponseDto checkIn(Authentication authentication) {
        User currentUser = getUser(authentication);

        if (attendanceRepository.findByEmployeeAndAttendanceDate(
                currentUser.getEmployee(), LocalDate.now()).isPresent()) {
            throw new IllegalStateException("Employee has already checked in today");
        }

        Attendance newAttendance = new Attendance();
        newAttendance.setEmployee(currentUser.getEmployee());
        newAttendance.setStatus(AttendanceStatus.PRESENT);
        newAttendance.setAttendanceDate(LocalDate.now());
        newAttendance.setCheckInTime(OffsetDateTime.now());
        newAttendance.setCreatedBy(currentUser);
        newAttendance.setUpdatedBy(currentUser);

        Attendance attendanceCheckedIn = attendanceRepository.save(newAttendance);
        return attendanceMapper.entityToResponseDto(attendanceCheckedIn);
    }

    @Override
    public AttendanceResponseDto checkOut(Authentication authentication) {
        User currentUser = getUser(authentication);

        Attendance existingAttendance = attendanceRepository.findByEmployeeAndAttendanceDate(
                        currentUser.getEmployee(), LocalDate.now())
                .orElseThrow(() -> new RuntimeException("Employee has not checked in yet today"));

        if (existingAttendance.getCheckOutTime() != null) {
            throw new RuntimeException("Employee has already checked out today");
        }

        existingAttendance.setCheckOutTime(OffsetDateTime.now());
        existingAttendance.setUpdatedBy(currentUser);

        Attendance attendanceCheckedOut = attendanceRepository.save(existingAttendance);
        return attendanceMapper.entityToResponseDto(attendanceCheckedOut);
    }

    private User getUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
