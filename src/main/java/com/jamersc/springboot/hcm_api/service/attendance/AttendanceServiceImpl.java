package com.jamersc.springboot.hcm_api.service.attendance;

import com.jamersc.springboot.hcm_api.dto.attendance.AttendanceDto;
import com.jamersc.springboot.hcm_api.dto.attendance.AttendanceResponseDto;
import com.jamersc.springboot.hcm_api.entity.Attendance;
import com.jamersc.springboot.hcm_api.entity.User;
import com.jamersc.springboot.hcm_api.mapper.AttendanceMapper;
import com.jamersc.springboot.hcm_api.repository.AttendanceRepository;
import com.jamersc.springboot.hcm_api.repository.EmployeeRepository;
import com.jamersc.springboot.hcm_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private static final Logger log = LoggerFactory.getLogger(AttendanceServiceImpl.class);
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final AttendanceMapper attendanceMapper;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository, UserRepository userRepository, AttendanceMapper attendanceMapper) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.attendanceMapper = attendanceMapper;
    }

    @Override
    public Page<AttendanceDto> getAllAttendance(Pageable pageable) {
        Page<Attendance> attendances = attendanceRepository.findAll(pageable);
        return attendances.map(attendanceMapper::entityToDto);
    }

    @Override
    public Page<AttendanceResponseDto> getMyAttendances(Pageable pageable, Authentication authentication) {
        User currentUser = getUser(authentication);

        Page<Attendance> myAttendances = attendanceRepository.findByEmployee(pageable,currentUser.getEmployee());

        return myAttendances.map(attendanceMapper::entityToResponseDto);
    }

    private User getUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User nof found"));
    }

    @Override
    public AttendanceResponseDto checkIn(Authentication authentication) {
        User currentUser = getUser(authentication);

        // checked if the employee already checked in
        if (attendanceRepository.findByEmployeeAndAttendanceDate(
                currentUser.getEmployee(), LocalDate.now()).isPresent()) {
            throw new IllegalStateException("Employee has already checked in today");
        }

        Attendance newAttendance = new Attendance();
        newAttendance.setEmployee(currentUser.getEmployee());
        newAttendance.setAttendanceDate(LocalDate.now());
        newAttendance.setCheckInTime(OffsetDateTime.now());

        return attendanceMapper.entityToResponseDto(attendanceRepository.save(newAttendance));
    }

    @Override
    public AttendanceResponseDto checkOut(Authentication authentication) {
        User currentUser = getUser(authentication);

        // find today's attendance record
        Attendance attendance = attendanceRepository.findByEmployeeAndAttendanceDate(
                currentUser.getEmployee(), LocalDate.now()).orElseThrow(()->new RuntimeException("Employee has not checked in yet today"));

        // check if they already checkout
        if (attendance.getCheckOutTime() != null) {
            throw new RuntimeException("Employee has already checked out today");
        }

        attendance.setCheckOutTime(OffsetDateTime.now());

        return attendanceMapper.entityToResponseDto(attendanceRepository.save(attendance));
    }


}
