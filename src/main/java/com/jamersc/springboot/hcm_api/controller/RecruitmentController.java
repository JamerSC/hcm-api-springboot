package com.jamersc.springboot.hcm_api.controller;

import com.jamersc.springboot.hcm_api.dto.applicant.ApplicantResponseDto;
import com.jamersc.springboot.hcm_api.dto.application.ApplicationResponseDto;
import com.jamersc.springboot.hcm_api.dto.application.ApplicationUpdateDto;
import com.jamersc.springboot.hcm_api.entity.ApplicationStatus;
import com.jamersc.springboot.hcm_api.service.applicant.ApplicantService;
import com.jamersc.springboot.hcm_api.service.application.ApplicationService;
import com.jamersc.springboot.hcm_api.utils.ApiResponse;
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
@RequestMapping("/api/v1/recruitments")
public class RecruitmentController {

    private final ApplicantService applicantService;
    private final ApplicationService applicationService;

    public RecruitmentController(ApplicantService applicantService, ApplicationService applicationService) {
        this.applicantService = applicantService;
        this.applicationService = applicationService;
    }

    @PreAuthorize("hasAuthority('VIEW_APPLICANTS')")
    @GetMapping("/applicants")
    public ResponseEntity<ApiResponse<Page<ApplicantResponseDto>>> getAllApplicants(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ApplicantResponseDto> retrievedApplicants = applicantService.getAllApplicants(
                search, dateFrom, dateTo, pageable
        );
        return ResponseEntity.ok(ApiResponse.<Page<ApplicantResponseDto>>builder()
                .success(true)
                .message("List of applicants retrieved successfully!")
                .data(retrievedApplicants)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('VIEW_APPLICANTS')")
    @GetMapping("/{id}/applicant")
    public ResponseEntity<ApiResponse<Optional<ApplicantResponseDto>>> getApplicant(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<Optional<ApplicantResponseDto>>builder()
                .success(true)
                .message("Applicant retrieved successfully!")
                .data(applicantService.getApplicant(id))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('VIEW_APPLICATIONS')")
    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<Page<ApplicationResponseDto>>> getAllApplications(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ApplicationResponseDto> retrievedApplications = applicationService.getAllApplications(
                search, status, dateFrom, dateTo, pageable
        );
        return ResponseEntity.ok(ApiResponse.<Page<ApplicationResponseDto>>builder()
                .success(true)
                .message("List of applications retrieved successfully!")
                .data(retrievedApplications)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('VIEW_APPLICATIONS')")
    @GetMapping("/{id}/application")
    public ResponseEntity<ApiResponse<Optional<ApplicationResponseDto>>> getApplication(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<Optional<ApplicationResponseDto>>builder()
                .success(true)
                .message("Application retrieved successfully!")
                .data(applicationService.getApplication(id))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('UPDATE_APPLICATIONS')")
    @PutMapping("/{id}/update-application")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> updateApplication(
            @PathVariable String id,
            @RequestBody ApplicationUpdateDto dto,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application updated successfully!")
                .data(applicationService.updateApplicationInformation(id, dto, authentication))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('APPROVE_APPLICATIONS')")
    @PatchMapping("/{id}/application/approve")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> approveApplication(
            @PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application status changed to Approved Application!")
                .data(applicationService.approveApplication(id, authentication))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('REJECT_APPLICATIONS')")
    @PatchMapping("/{id}/application/reject")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> rejectApplication(
            @PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application status changed to Rejected Application!")
                .data(applicationService.rejectApplication(id, authentication))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('INITIAL_QUALIFICATION')")
    @PatchMapping("/{id}/application/initial-qualification")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> initialQualification(
            @PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application changed status to Initial Qualification!")
                .data(applicationService.initialQualification(id, authentication))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('FIRST_INTERVIEW')")
    @PatchMapping("/{id}/application/first-interview")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> firstInterview(
            @PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application status changed to First Interview!")
                .data(applicationService.firstInterview(id, authentication))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('SECOND_INTERVIEW')")
    @PatchMapping("/{id}/application/second-interview")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> secondInterview(
            @PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application status changed to Second Interview!")
                .data(applicationService.secondInterview(id, authentication))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('CONTRACT_PROPOSAL')")
    @PatchMapping("/{id}/application/contract-proposal")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> contractProposal(
            @PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application status changed to Contract Proposal!")
                .data(applicationService.contractProposal(id, authentication))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('CONTRACT_SIGNED')")
    @PatchMapping("/{id}/application/contract-signed")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> contractSigned(
            @PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application status changed to Contract Signed!")
                .data(applicationService.contractSigned(id, authentication))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('HIRE_APPLICANTS')")
    @PatchMapping("/{id}/application/hire")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> hireApplicant(
            @PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application status changed to Hired Applicant!")
                .data(applicationService.hireApplication(id, authentication))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }
}
