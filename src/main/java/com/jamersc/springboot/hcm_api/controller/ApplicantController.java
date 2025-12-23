package com.jamersc.springboot.hcm_api.controller;

import com.jamersc.springboot.hcm_api.dto.applicant.ApplicantProfileDto;
import com.jamersc.springboot.hcm_api.dto.applicant.ApplicantResponseDto;
import com.jamersc.springboot.hcm_api.dto.application.ApplicationResponseDto;
import com.jamersc.springboot.hcm_api.dto.job.JobResponseDto;
import com.jamersc.springboot.hcm_api.service.applicant.ApplicantService;
import com.jamersc.springboot.hcm_api.service.job.JobService;
import com.jamersc.springboot.hcm_api.utils.ApiResponse;
import jakarta.validation.Valid;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/applicants")
public class ApplicantController {

    private final ApplicantService applicantService;
    private final JobService jobService;

    public ApplicantController(ApplicantService applicantService, JobService jobService) {
        this.applicantService = applicantService;
        this.jobService = jobService;
    }

    @PreAuthorize("hasAuthority('VIEW_OPEN_JOBS')")
    @GetMapping("/open/jobs")
    public ResponseEntity<ApiResponse<Page<JobResponseDto>>> getOpenJobs(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @PageableDefault(page = 0, size = 10, sort = "postedDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<JobResponseDto> retrievedOpenJobs = jobService.getOpenJobs(
                search,
                dateFrom,
                dateTo,
                pageable
        );

        ApiResponse<Page<JobResponseDto>> response = ApiResponse.<Page<JobResponseDto>>builder()
                .success(true)
                .message("List of open jobs retrieved successfully!")
                .data(retrievedOpenJobs)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('APPLY_JOBS')")
    @PostMapping("/jobs/{id}/apply")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> applyForJob(
            @PathVariable Long id, Authentication authentication) {
        ApplicationResponseDto applicationSubmitted = applicantService.apply(id, authentication);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Job application submitted successfully!")
                .data(applicationSubmitted)
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VIEW_APPLIED_JOBS')")
    @GetMapping("/applications/jobs-applied")
    public ResponseEntity<ApiResponse<Page<ApplicationResponseDto>>> getAllApplicantJobsApplied(
            @PageableDefault(page = 0, size = 10, sort = "status") Pageable pageable,
            Authentication authentication)
    {
        Page<ApplicationResponseDto> appliedJobs = applicantService.getAllApplicantJobsApplied(pageable, authentication);
        ApiResponse<Page<ApplicationResponseDto>> response = ApiResponse.<Page<ApplicationResponseDto>>builder()
                .success(true)
                .message("Applicant jobs applied retrieved successfully!")
                .data(appliedJobs)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VIEW_APPLIED_JOBS')")
    @GetMapping("/application/{id}/view")
    public ResponseEntity<ApiResponse<Optional<ApplicationResponseDto>>> getAppliedJob(
            @PathVariable Long id, Authentication authentication) {
        Optional<ApplicationResponseDto> appliedJob = applicantService
                .getApplicantJobsApplied(id, authentication);
        ApiResponse<Optional<ApplicationResponseDto>> response = ApiResponse.<Optional<ApplicationResponseDto>>builder()
                .success(true)
                .message("Job application retrieved successfully!")
                .data(appliedJob)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('WITHDRAW_APPLIED_JOBS')")
    @PatchMapping("/application/{id}/withdraw")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> withdrawApplication(
            @PathVariable Long id, Authentication authentication) {
        ApplicationResponseDto withdrawn = applicantService.withdrawApplication(id, authentication);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Job application withdrawn successfully!")
                .data(withdrawn)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('MY_APPLICANT_PROFILE')")
    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<ApplicantResponseDto>> myApplicantProfile(
            Authentication authentication) {
        ApplicantResponseDto retrievedProfile = applicantService.getMyApplicantProfile(authentication);
        ApiResponse<ApplicantResponseDto> response = ApiResponse.<ApplicantResponseDto>builder()
                .success(true)
                .message("Applicant profile retrieved successfully!")
                .data(retrievedProfile)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('UPDATE_APPLICANT_PROFILE')")
    @PutMapping("/update-profile")
    public ResponseEntity<ApiResponse<ApplicantResponseDto>> updateMyApplicantProfile(
            @RequestBody ApplicantProfileDto profileDto,
            Authentication authentication) {
        ApplicantResponseDto updatedProfile = applicantService.updateMyApplicantProfile(profileDto, authentication);
        ApiResponse<ApplicantResponseDto> response = ApiResponse.<ApplicantResponseDto>builder()
                .success(true)
                .message("Applicant profile updated successfully!")
                .data(updatedProfile)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('UPLOAD_RESUME')")
    // Endpoint for CV/Resume upload
    @PostMapping("/profile/upload-resume")
    public ResponseEntity<ApiResponse<ApplicantResponseDto>> uploadResume(
            @Valid @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        ApplicantResponseDto uploadedResume = applicantService.uploadResume(file, authentication);
        ApiResponse<ApplicantResponseDto> response = ApiResponse.<ApplicantResponseDto>builder()
                .success(true)
                .message("Applicant resume uploaded successfully!")
                .data(uploadedResume)
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('DELETE_ACCOUNT')")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<String>> deleteMyAccount(Authentication authentication) {
        applicantService.deleteApplicantAccount(authentication);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Applicant user account deleted successfully.")
                .data(null)
                .status(HttpStatus.NO_CONTENT.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
