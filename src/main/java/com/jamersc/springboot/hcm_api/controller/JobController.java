package com.jamersc.springboot.hcm_api.controller;

import com.jamersc.springboot.hcm_api.dto.job.JobCreateDto;
import com.jamersc.springboot.hcm_api.dto.job.JobDto;
import com.jamersc.springboot.hcm_api.dto.job.JobPatchDto;
import com.jamersc.springboot.hcm_api.dto.job.JobResponseDto;
import com.jamersc.springboot.hcm_api.service.job.JobService;
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
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PreAuthorize("hasAuthority('VIEW_JOBS')")
    @GetMapping("/")
    public ResponseEntity<ApiResponse<Page<JobDto>>> getAllJobs(
            @PageableDefault(page = 0, size = 10, sort = "title") Pageable pageable) {
        Page<JobDto> retrievedJobs = jobService.getAllJob(pageable);
        ApiResponse<Page<JobDto>> response = ApiResponse.<Page<JobDto>>builder()
                .success(true)
                .message("List of jobs retrieved successfully!")
                .data(retrievedJobs)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CREATE_JOBS')")
    @PostMapping("/")
    public ResponseEntity<ApiResponse<JobResponseDto>> createJob(
            @Valid @RequestBody JobCreateDto createDTO,
            Authentication authentication) {
        JobResponseDto createdJob = jobService.createJob(createDTO, authentication);
        ApiResponse<JobResponseDto> response = ApiResponse.<JobResponseDto>builder()
                .success(true)
                .message("Job created successfully!")
                .data(createdJob)
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VIEW_JOBS')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<JobResponseDto>>> getJob(@PathVariable Long id) {
        Optional<JobResponseDto> retrievedJob = jobService.getJob(id);
        ApiResponse<Optional<JobResponseDto>> response = ApiResponse.<Optional<JobResponseDto>>builder()
                .success(true)
                .message("Job retrieved successfully!")
                .data(retrievedJob)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('UPDATE_JOBS')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponseDto>> updateJob(
            @PathVariable Long id, @RequestBody JobPatchDto dto, Authentication authentication) {
        JobResponseDto updatedJob = jobService.updateJob(id, dto, authentication);
        ApiResponse<JobResponseDto> response = ApiResponse.<JobResponseDto>builder()
                .success(true)
                .message("Job updated successfully!")
                .data(updatedJob)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('OPEN_JOBS')")
    @PatchMapping("/{id}/open")
    public ResponseEntity<ApiResponse<JobResponseDto>> openJob(
            @PathVariable Long id, Authentication authentication) {
        JobResponseDto openedJob = jobService.openJob(id, authentication);
        ApiResponse<JobResponseDto> response = ApiResponse.<JobResponseDto>builder()
                .success(true)
                .message("Job opened successfully!")
                .data(openedJob)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAuthority('CLOSE_JOBS')")
    @PatchMapping("/{id}/close")
    public ResponseEntity<ApiResponse<JobResponseDto>> closeJob(
            @PathVariable Long id, Authentication authentication) {
        JobResponseDto closedJob = jobService.closeJob(id, authentication);
        ApiResponse<JobResponseDto> response = ApiResponse.<JobResponseDto>builder()
                .success(true)
                .message("Job closed successfully!")
                .data(closedJob)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('FILL_JOBS')")
    @PatchMapping("/{id}/filled")
    public ResponseEntity<ApiResponse<JobResponseDto>> fillJob(
            @PathVariable Long id, Authentication authentication) {
        JobResponseDto filledJob = jobService.fillJob(id, authentication);
        ApiResponse<JobResponseDto> response = ApiResponse.<JobResponseDto>builder()
                .success(true)
                .message("Job filled successfully!")
                .data(filledJob)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    // todo improve validation
    @PreAuthorize("hasAuthority('ARCHIVE_JOBS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> archiveJob(
            @PathVariable Long id, Authentication authentication) {
        Optional<JobResponseDto> job = jobService.getJob(id);
        if (job.isEmpty()) {
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .success(true)
                    .message("Job not found!")
                    .data(null)
                    .status(HttpStatus.NOT_FOUND.value())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(response);
        }

        jobService.archiveJob(id, authentication);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Job archived successfully!")
                .data(null)
                .status(HttpStatus.NO_CONTENT.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    // todo improve validation
    @PreAuthorize("hasAuthority('UNARCHIVED_JOBS')")
    @PatchMapping("/{id}/unarchived")
    public String unarchivedJob(@PathVariable Long id, Authentication authentication) {
        jobService.unarchivedJob(id, authentication);
        return "Job unarchived successfully!";
    }
}
