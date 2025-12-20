package com.jamersc.springboot.hcm_api.controller;

import com.jamersc.springboot.hcm_api.dto.applicant.ApplicantResponseDto;
import com.jamersc.springboot.hcm_api.dto.application.ApplicationResponseDto;
import com.jamersc.springboot.hcm_api.dto.application.ApplicationUpdateDto;
import com.jamersc.springboot.hcm_api.dto.leave.LeaveResponseDto;
import com.jamersc.springboot.hcm_api.service.applicant.ApplicantService;
import com.jamersc.springboot.hcm_api.service.application.ApplicationService;
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
            @PageableDefault(page = 0, size = 10, sort = "lastName") Pageable pageable) {
        Page<ApplicantResponseDto> retrievedApplicants = applicantService.getAllApplicant(pageable);
        ApiResponse<Page<ApplicantResponseDto>> response = ApiResponse.<Page<ApplicantResponseDto>>builder()
                .success(true)
                .message("List of applicants retrieved successfully!")
                .data(retrievedApplicants)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VIEW_APPLICANTS')")
    @GetMapping("/{id}/applicant")
    public ResponseEntity<ApiResponse<Optional<ApplicantResponseDto>>> getApplicantById(
            @PathVariable Long id) {
        Optional<ApplicantResponseDto> retrievedApplicant = applicantService.getApplicantById(id);
        ApiResponse<Optional<ApplicantResponseDto>> response = ApiResponse.<Optional<ApplicantResponseDto>>builder()
                .success(true)
                .message("Applicant retrieved successfully!")
                .data(retrievedApplicant)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    /*** APPLICATION **/
    @PreAuthorize("hasAuthority('VIEW_APPLICATIONS')")
    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<Page<ApplicationResponseDto>>> getAllApplicationsSubmitted(
            @PageableDefault(page = 0, size = 10, sort = "status") Pageable pageable) {
        Page<ApplicationResponseDto> retrievedSubmittedApplications = applicationService.getAllApplication(pageable);
        ApiResponse<Page<ApplicationResponseDto>> response = ApiResponse.<Page<ApplicationResponseDto>>builder()
                .success(true)
                .message("List of submitted applications retrieved successfully!")
                .data(retrievedSubmittedApplications)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VIEW_APPLICATIONS')")
    @GetMapping("/{id}/application")
    public ResponseEntity<ApiResponse<Optional<ApplicationResponseDto>>> getApplication(
            @PathVariable Long id) {
        Optional<ApplicationResponseDto> retrievedApplication = applicationService.getApplicationById(id);
        ApiResponse<Optional<ApplicationResponseDto>> response = ApiResponse.<Optional<ApplicationResponseDto>>builder()
                .success(true)
                .message("Application retrieved successfully!")
                .data(retrievedApplication)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('UPDATE_APPLICATIONS')")
    @PutMapping("/{id}/update-application")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> updateApplication(
            @PathVariable Long id, @RequestBody ApplicationUpdateDto dto, Authentication authentication) {
        ApplicationResponseDto updatedApplication = applicationService.updateApplicationInformation(id, dto, authentication);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application updated successfully!")
                .data(updatedApplication)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('APPROVE_APPLICATIONS')")
    @PatchMapping("/{id}/application/approve")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> approveApplication(
            @PathVariable Long id, Authentication authentication) {
        ApplicationResponseDto changedStatus = applicationService.approveApplication(id, authentication);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application status changed to Approved Application!")
                .data(changedStatus)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('REJECT_APPLICATIONS')")
    @PatchMapping("/{id}/application/reject")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> rejectApplication(
            @PathVariable Long id, Authentication authentication) {
        ApplicationResponseDto changedStatus = applicationService.rejectApplication(id, authentication);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application status changed to Rejected Application!")
                .data(changedStatus)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('INITIAL_QUALIFICATION')")
    @PatchMapping("/{id}/application/initial-qualification")
    public ResponseEntity<ApiResponse<ApplicationResponseDto> > initialQualification(
            @PathVariable Long id, Authentication authentication) {
        ApplicationResponseDto changedStatus = applicationService.initialQualification(id, authentication);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application changed status to Initial Qualification!")
                .data(changedStatus)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('FIRST_INTERVIEW')")
    @PatchMapping("/{id}/application/first-interview")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> firstInterview(
            @PathVariable Long id, Authentication authentication) {
        ApplicationResponseDto changedStatus = applicationService.firstInterview(id, authentication);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application status changed to First Interview!")
                .data(changedStatus)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SECOND_INTERVIEW')")
    @PatchMapping("/{id}/application/second-interview")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> secondInterview(
            @PathVariable Long id, Authentication authentication) {
        ApplicationResponseDto changedStatus = applicationService.secondInterview(id, authentication);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application status changed to Second Interview!")
                .data(changedStatus)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CONTRACT_PROPOSAL')")
    @PatchMapping("/{id}/application/contract-proposal")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> contractProposal(
            @PathVariable Long id, Authentication authentication) {
        ApplicationResponseDto changedStatus = applicationService.contractProposal(id, authentication);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application status changed to Contract Proposal!")
                .data(changedStatus)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CONTRACT_SIGNED')")
    @PatchMapping("/{id}/application/contract-signed")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> contractSigned(
            @PathVariable Long id, Authentication authentication) {
        ApplicationResponseDto changedStatus = applicationService.contractSigned(id, authentication);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application status changed to Contract Signed!")
                .data(changedStatus)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('HIRE_APPLICANTS')")
    @PatchMapping("/{id}/application/hire")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> hireApplicant(
            @PathVariable Long id, Authentication authentication) {
        ApplicationResponseDto changedStatus = applicationService.hireApplication(id, authentication);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application status changed to Hired Applicant!")
                .data(changedStatus)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
