package com.jamersc.springboot.hcm_api.service.leave;

import com.jamersc.springboot.hcm_api.dto.leave.LeaveCreateDto;
import com.jamersc.springboot.hcm_api.dto.leave.LeaveResponseDto;
import com.jamersc.springboot.hcm_api.dto.leave.LeaveUpdateDto;
import com.jamersc.springboot.hcm_api.entity.Leave;
import com.jamersc.springboot.hcm_api.entity.LeaveStatus;
import com.jamersc.springboot.hcm_api.entity.LeaveType;
import com.jamersc.springboot.hcm_api.entity.User;
import com.jamersc.springboot.hcm_api.mapper.LeaveMapper;
import com.jamersc.springboot.hcm_api.repository.EmployeeRepository;
import com.jamersc.springboot.hcm_api.repository.LeaveRepository;
import com.jamersc.springboot.hcm_api.repository.LeaveSpecification;
import com.jamersc.springboot.hcm_api.repository.UserRepository;
import com.jamersc.springboot.hcm_api.service.security.HashidService;
import com.jamersc.springboot.hcm_api.service.user.UserService;
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
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final UserService userService;
    private final LeaveMapper leaveMapper;
    private final HashidService hashidService;

    @Override
    public Page<LeaveResponseDto> getAllLeaveRequest(
            String search,
            LeaveType leaveType,
            LeaveStatus leaveStatus,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    ) {
        Specification<Leave> spec = Specification.allOf(
                LeaveSpecification.search(search),
                LeaveSpecification.hasType(leaveType),
                LeaveSpecification.hasStatus(leaveStatus),
                LeaveSpecification.dateRange(dateFrom, dateTo)
        );

        Page<Leave> leaves = leaveRepository.findAll(spec, pageable);
        return leaves.map(leaveMapper::entityToResponseDto);
    }

    @Override
    public Optional<LeaveResponseDto> getLeaveRequest(String id) {
        Long decodedId = hashidService.decode(id);
        return Optional.of(leaveRepository.findById(decodedId)
                        .map(leaveMapper::entityToResponseDto))
                .orElseThrow(()-> new RuntimeException("Leave request not found"));
    }

    @Override
    public Page<LeaveResponseDto> getMyLeaveRequests(
            String search,
            LeaveType leaveType,
            LeaveStatus leaveStatus,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable,
            Authentication authentication
    ) {
        User currentUser = userService.getUserAuthentication(authentication);

        Specification<Leave> spec = Specification.allOf(
                LeaveSpecification.byEmployee(currentUser.getEmployee()),
                LeaveSpecification.search(search),
                LeaveSpecification.hasType(leaveType),
                LeaveSpecification.hasStatus(leaveStatus),
                LeaveSpecification.dateRange(dateFrom, dateTo)
        );

        Page<Leave> myLeaveRequests = leaveRepository.findAll(spec, pageable);
        return myLeaveRequests.map(leaveMapper::entityToResponseDto);
    }

    @Override
    public LeaveResponseDto createLeaveRequest(LeaveCreateDto dto, Authentication authentication) {
        User currentUser = userService.getUserAuthentication(authentication);

        Leave leaveRequest = new Leave();
        leaveRequest.setEmployee(currentUser.getEmployee());
        leaveRequest.setLeaveType(dto.getLeaveType());
        leaveRequest.setStartDate(dto.getStartDate());
        leaveRequest.setEndDate(dto.getEndDate());
        leaveRequest.setReason(dto.getReason());
        leaveRequest.setStatus(LeaveStatus.SUBMITTED);
        leaveRequest.setSubmittedAt(OffsetDateTime.now());
        leaveRequest.setCreatedBy(currentUser);
        leaveRequest.setUpdatedBy(currentUser);

        Leave submitted = leaveRepository.save(leaveRequest);
        return leaveMapper.entityToResponseDto(submitted);
    }

    @Override
    public LeaveResponseDto cancelLeaveRequest(String id, Authentication authentication) {
        User currentUser = userService.getUserAuthentication(authentication);
        Long decodedId = hashidService.decode(id);

        Leave leave = leaveRepository.findById(decodedId)
                .orElseThrow(()->new RuntimeException("Leave not found"));
        leave.setStatus(LeaveStatus.CANCELLED);
        leave.setUpdatedBy(currentUser);

        Leave cancelledLeave = leaveRepository.save(leave);
        return leaveMapper.entityToResponseDto(cancelledLeave);
    }

    @Override
    public LeaveResponseDto updateLeaveRequest(String id, LeaveUpdateDto dto, Authentication authentication) {
        User currentUser = userService.getUserAuthentication(authentication);
        Long decodedId = hashidService.decode(id);

        Leave leaveRequest = leaveRepository.findById(decodedId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (!leaveRequest.getEmployee().getId().equals(currentUser.getEmployee().getId())) {
            throw new RuntimeException("You are not allowed to update this leave request");
        }

        leaveRequest.setLeaveType(dto.getLeaveType());
        leaveRequest.setStartDate(dto.getStartDate());
        leaveRequest.setEndDate(dto.getEndDate());
        leaveRequest.setReason(dto.getReason());
        leaveRequest.setUpdatedBy(currentUser);
        leaveRequest.setUpdatedAt(OffsetDateTime.now());

        Leave updated = leaveRepository.save(leaveRequest);
        return leaveMapper.entityToResponseDto(updated);
    }

    @Override
    public LeaveResponseDto approveLeaveRequest(String id, Authentication authentication) {
        User currentUser = userService.getUserAuthentication(authentication);
        Long decodedId = hashidService.decode(id);

        Leave requestedLeave = leaveRepository.findById(decodedId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        requestedLeave.setStatus(LeaveStatus.APPROVED);
        requestedLeave.setApprovedBy(currentUser);
        requestedLeave.setUpdatedBy(currentUser);

        Leave approvedLeave = leaveRepository.save(requestedLeave);
        return leaveMapper.entityToResponseDto(approvedLeave);
    }

    @Override
    public LeaveResponseDto rejectLeaveRequest(String id, Authentication authentication) {
        User currentUser = userService.getUserAuthentication(authentication);
        Long decodedId = hashidService.decode(id);

        Leave requestedLeave = leaveRepository.findById(decodedId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        requestedLeave.setStatus(LeaveStatus.REJECTED);
        requestedLeave.setApprovedBy(currentUser);
        requestedLeave.setUpdatedBy(currentUser);

        Leave rejectedLeave = leaveRepository.save(requestedLeave);
        return leaveMapper.entityToResponseDto(rejectedLeave);
    }

    @Override
    public void archiveLeaveRequest(String id, Authentication authentication) {
        Long decodedId = hashidService.decode(id);
        leaveRepository.deleteById(decodedId);
    }

    @Override
    public void unarchivedLeaveRequest(String id, Authentication authentication) {
        Long decodedId = hashidService.decode(id);
        // Implement unarchive implementation logic using decodedId
    }
}
