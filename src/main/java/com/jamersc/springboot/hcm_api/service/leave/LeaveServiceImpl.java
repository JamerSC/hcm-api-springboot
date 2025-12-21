package com.jamersc.springboot.hcm_api.service.leave;

import com.jamersc.springboot.hcm_api.dto.leave.LeaveCreateDto;
import com.jamersc.springboot.hcm_api.dto.leave.LeaveResponseDto;
import com.jamersc.springboot.hcm_api.dto.leave.LeaveUpdateDto;
import com.jamersc.springboot.hcm_api.entity.Leave;
import com.jamersc.springboot.hcm_api.entity.LeaveStatus;
import com.jamersc.springboot.hcm_api.entity.User;
import com.jamersc.springboot.hcm_api.mapper.LeaveMapper;
import com.jamersc.springboot.hcm_api.repository.EmployeeRepository;
import com.jamersc.springboot.hcm_api.repository.LeaveRepository;
import com.jamersc.springboot.hcm_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class LeaveServiceImpl implements LeaveService {

    private static final Logger log = LoggerFactory.getLogger(LeaveServiceImpl.class);
    private final LeaveRepository leaveRepository;
    private final UserRepository userRepository;
    private final LeaveMapper leaveMapper;

    public LeaveServiceImpl(LeaveRepository leaveRepository, UserRepository userRepository, EmployeeRepository employeeRepository, LeaveMapper leaveMapper) {
        this.leaveRepository = leaveRepository;
        this.userRepository = userRepository;
        this.leaveMapper = leaveMapper;
    }


    @Override
    public Page<LeaveResponseDto> getAllLeaveRequest(Pageable pageable) {
        Page<Leave> leaves = leaveRepository.findAll(pageable);
        return leaves.map(leaveMapper::entityToResponseDto);
    }

    @Override
    public Optional<LeaveResponseDto> getLeaveRequest(Long id) {
        return Optional.of(leaveRepository.findById(id)
                .map(leaveMapper::entityToResponseDto))
                .orElseThrow(()-> new RuntimeException("Leave request not found"));
    }

    @Override
    public Page<LeaveResponseDto> getMyLeaveRequests(Pageable pageable, Authentication authentication) {
        User currentUser = getUser(authentication);
        Page<Leave> myLeaveRequests = leaveRepository.findByEmployee(pageable, currentUser.getEmployee());
        return myLeaveRequests.map(leaveMapper::entityToResponseDto);
    }

    @Override
    public LeaveResponseDto createLeaveRequest(LeaveCreateDto dto, Authentication authentication) {
        User currentUser = getUser(authentication);

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
    public LeaveResponseDto cancelLeaveRequest(Long id, Authentication authentication) {
        User currentUser = getUser(authentication);
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Leave not found"));
        leave.setStatus(LeaveStatus.CANCELLED);
        leave.setUpdatedBy(currentUser);
        Leave cancelledLeave = leaveRepository.save(leave);
        return leaveMapper.entityToResponseDto(cancelledLeave);
    }

    @Override
    public LeaveResponseDto updateLeaveRequest(Long id, LeaveUpdateDto dto, Authentication authentication) {
        User currentUser = getUser(authentication);

        // 1. Find existing leave request
        Leave leaveRequest = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        // 2. Ensure the logged-in user owns the leave request (optional security check)
        if (!leaveRequest.getEmployee().getId().equals(currentUser.getEmployee().getId())) {
            throw new RuntimeException("You are not allowed to update this leave request");
        }
        // 3. Update fields
        leaveRequest.setLeaveType(dto.getLeaveType());
        leaveRequest.setStartDate(dto.getStartDate());
        leaveRequest.setEndDate(dto.getEndDate());
        leaveRequest.setReason(dto.getReason());
        leaveRequest.setUpdatedBy(currentUser);
        leaveRequest.setUpdatedAt(OffsetDateTime.now());

        // 4. Save updated entity
        Leave updated = leaveRepository.save(leaveRequest);

        return leaveMapper.entityToResponseDto(updated);
    }

    @Override
    public LeaveResponseDto approveLeaveRequest(Long id, Authentication authentication) {
        User currentUser = getUser(authentication);

        Leave requestedLeave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        requestedLeave.setStatus(LeaveStatus.APPROVED);
        requestedLeave.setApprovedBy(currentUser);
        requestedLeave.setUpdatedBy(currentUser);

        Leave approvedLeave = leaveRepository.save(requestedLeave);

        return leaveMapper.entityToResponseDto(approvedLeave);
    }

    @Override
    public LeaveResponseDto rejectLeaveRequest(Long id, Authentication authentication) {
        User currentUser = getUser(authentication);

        Leave requestedLeave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        requestedLeave.setStatus(LeaveStatus.REJECTED);
        requestedLeave.setApprovedBy(currentUser);
        requestedLeave.setUpdatedBy(currentUser);

        Leave rejectedLeave = leaveRepository.save(requestedLeave);

        return leaveMapper.entityToResponseDto(rejectedLeave);
    }

    @Override
    public void archiveLeaveRequest(Long id, Authentication authentication) {
        leaveRepository.deleteById(id);
    }

    @Override
    public void unarchivedLeaveRequest(Long id, Authentication authentication) {
        //
    }

    private User getUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
