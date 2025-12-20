package com.jamersc.springboot.hcm_api.controller;

import com.jamersc.springboot.hcm_api.dto.leave.LeaveCreateDto;
import com.jamersc.springboot.hcm_api.dto.leave.LeaveResponseDto;
import com.jamersc.springboot.hcm_api.dto.leave.LeaveUpdateDto;
import com.jamersc.springboot.hcm_api.service.leave.LeaveService;
import com.jamersc.springboot.hcm_api.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PreAuthorize("hasAuthority('VIEW_LEAVES')")
    @GetMapping("/")
    public ResponseEntity<ApiResponse<Page<LeaveResponseDto>>> getAllLeaveRequests(
            @PageableDefault(page = 0, size = 10, sort="submittedAt") Pageable pageable) {
        Page<LeaveResponseDto> retrievedLeaveRequests = leaveService.getAllLeaveRequest(pageable);
        ApiResponse<Page<LeaveResponseDto>> response = ApiResponse.<Page<LeaveResponseDto>>builder()
                .success(true)
                .message("List of leave request retrieved successfully!")
                .data(retrievedLeaveRequests)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CREATE_LEAVES')")
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<LeaveResponseDto>> createLeaveRequest(
            @Valid @RequestBody LeaveCreateDto createDTO,
            Authentication authentication) {
        LeaveResponseDto submittedLeaveRequest = leaveService.createLeaveRequest(createDTO, authentication);
        ApiResponse<LeaveResponseDto> response = ApiResponse.<LeaveResponseDto>builder()
                .success(true)
                .message("Leave request submitted successfully!")
                .data(submittedLeaveRequest)
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VIEW_LEAVES')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<LeaveResponseDto>>> getLeaveRequest(@PathVariable Long id) {
        Optional<LeaveResponseDto> retrievedLeaveRequest = leaveService.getLeaveRequest(id);
        ApiResponse<Optional<LeaveResponseDto>> response = ApiResponse.<Optional<LeaveResponseDto>>builder()
                .success(true)
                .message("Leave request retrieved successfully!")
                .data(retrievedLeaveRequest)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CANCEL_LEAVES')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<LeaveResponseDto>> cancelLeaveRequest(
            @PathVariable Long id, Authentication authentication) {
        LeaveResponseDto cancelledLeaveRequest = leaveService.cancelLeaveRequest(id, authentication);
        ApiResponse<LeaveResponseDto> response = ApiResponse.<LeaveResponseDto>builder()
                .success(true)
                .message("Leave request cancelled successfully!")
                .data(cancelledLeaveRequest)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VIEW_MY_LEAVES')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Page<LeaveResponseDto>>> myLeaveRequests(
            @PageableDefault(page = 0, size = 10, sort="submittedAt") Pageable pageable,
            Authentication authentication
    ) {
        Page<LeaveResponseDto> myRequestedLeaves = leaveService
                                             .getMyLeaveRequests(pageable, authentication);
        ApiResponse<Page<LeaveResponseDto>> response = ApiResponse.<Page<LeaveResponseDto>>builder()
                .success(true)
                .message("My leave request retrieved successfully!")
                .data(myRequestedLeaves)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAuthority('UPDATE_LEAVES')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaveResponseDto> > updateLeaveRequest(
            @PathVariable Long id, @Valid @RequestBody LeaveUpdateDto dto,
            Authentication authentication) {
        LeaveResponseDto updatedLeaveRequest = leaveService.updateLeaveRequest(id, dto, authentication);
        ApiResponse<LeaveResponseDto> response = ApiResponse.<LeaveResponseDto>builder()
                .success(true)
                .message("Leave request updated successfully!")
                .data(updatedLeaveRequest)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('APPROVE_LEAVES')")
    @PatchMapping("/{id}/approve-leave")
    public ResponseEntity<ApiResponse<LeaveResponseDto>> approveLeaveRequest(
            @PathVariable Long id, Authentication authentication) {
        LeaveResponseDto approvedLeaveRequest = leaveService.approveLeaveRequest(id, authentication);
        ApiResponse<LeaveResponseDto> response = ApiResponse.<LeaveResponseDto>builder()
                .success(true)
                .message("Leave request approved successfully!")
                .data(approvedLeaveRequest)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('REJECT_LEAVES')")
    @PatchMapping("/{id}/reject-leave")
    public ResponseEntity<ApiResponse<LeaveResponseDto>> rejectLeaveRequest(
            @PathVariable Long id, Authentication authentication) {
        LeaveResponseDto rejectedLeaveRequest = leaveService.rejectLeaveRequest(id, authentication);
        ApiResponse<LeaveResponseDto> response = ApiResponse.<LeaveResponseDto>builder()
                .success(true)
                .message("Leave request rejected successfully!")
                .data(rejectedLeaveRequest)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    // todo improve validation
    @PreAuthorize("hasAuthority('ARCHIVE_LEAVES')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> archiveLeaveRequest(
            @PathVariable Long id, Authentication authentication) {
        Optional<LeaveResponseDto> tempLeaveRequest = leaveService.getLeaveRequest(id);

        if (tempLeaveRequest.isEmpty()) {
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .success(true)
                    .message("Leave request not found!")
                    .data(null)
                    .status(HttpStatus.NOT_FOUND.value())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(response);
        }

        leaveService.archiveLeaveRequest(id, authentication);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Leave request archived successfully!")
                .data(null)
                .status(HttpStatus.NO_CONTENT.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    // todo improve validation
    @PreAuthorize("hasAuthority('UNARCHIVED_LEAVES')")
    @PatchMapping("/{id}/unarchived")
    public String unarchivedLeaveRequest(@PathVariable Long id, Authentication authentication) {
        leaveService.unarchivedLeaveRequest(id, authentication);
        return "Leave request unarchived successfully!";
    }
}
