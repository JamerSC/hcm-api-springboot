package com.jamersc.springboot.hcm_api.service.leave;

import com.jamersc.springboot.hcm_api.dto.leave.LeaveCreateDto;
import com.jamersc.springboot.hcm_api.dto.leave.LeaveResponseDto;
import com.jamersc.springboot.hcm_api.dto.leave.LeaveUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface LeaveService {
    Page<LeaveResponseDto> getAllLeaveRequest(Pageable pageable);
    Optional<LeaveResponseDto> getLeaveRequest(Long id);
    Page<LeaveResponseDto> getMyLeaveRequests(Pageable pageable, Authentication authentication);
    LeaveResponseDto createLeaveRequest(LeaveCreateDto dto, Authentication authentication);
    LeaveResponseDto cancelLeaveRequest(Long id, Authentication authentication);
    LeaveResponseDto updateLeaveRequest(Long id, LeaveUpdateDto dto, Authentication authentication);
    LeaveResponseDto approveLeaveRequest(Long id, Authentication authentication);
    LeaveResponseDto rejectLeaveRequest(Long id, Authentication authentication);
    void archiveLeaveRequest(Long id, Authentication authentication);
    void unarchivedLeaveRequest(Long id, Authentication authentication);

}
