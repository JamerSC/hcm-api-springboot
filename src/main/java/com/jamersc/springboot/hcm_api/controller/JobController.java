package com.jamersc.springboot.hcm_api.controller;

import com.jamersc.springboot.hcm_api.dto.job.JobCreateDto;
import com.jamersc.springboot.hcm_api.dto.job.JobDto;
import com.jamersc.springboot.hcm_api.dto.job.JobPatchDto;
import com.jamersc.springboot.hcm_api.dto.job.JobResponseDto;
import com.jamersc.springboot.hcm_api.entity.JobStatus;
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

import java.time.LocalDate;
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
    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobDto>>> getAllJobs(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String departmentId, // Hash ID
            @RequestParam(required = false) JobStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable)
    {
        Page<JobDto> retrievedJobs = jobService.getAllJob(
                search, departmentId, status, dateFrom, dateTo, pageable
        );
        return ResponseEntity.ok(ApiResponse.<Page<JobDto>>builder()
                .success(true)
                .message("List of jobs retrieved successfully!")
                .data(retrievedJobs)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('CREATE_JOBS')")
    @PostMapping
    public ResponseEntity<ApiResponse<JobResponseDto>> createJob(
            @Valid @RequestBody JobCreateDto createDTO,
            Authentication authentication) {
        JobResponseDto createdJob = jobService.createJob(createDTO, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<JobResponseDto>builder()
                .success(true)
                .message("Job created successfully!")
                .data(createdJob)
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('VIEW_JOBS')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<JobResponseDto>>> getJob(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<Optional<JobResponseDto>>builder()
                .success(true)
                .message("Job retrieved successfully!")
                .data(jobService.getJob(id))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('UPDATE_JOBS')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponseDto>> updateJob(
            @PathVariable String id,
            @RequestBody JobPatchDto dto,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<JobResponseDto>builder()
                .success(true)
                .message("Job updated successfully!")
                .data(jobService.updateJob(id, dto, authentication))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('OPEN_JOBS')")
    @PatchMapping("/{id}/open")
    public ResponseEntity<ApiResponse<JobResponseDto>> openJob(
            @PathVariable String id,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<JobResponseDto>builder()
                .success(true)
                .message("Job opened successfully!")
                .data(jobService.openJob(id, authentication))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('CLOSE_JOBS')")
    @PatchMapping("/{id}/close")
    public ResponseEntity<ApiResponse<JobResponseDto>> closeJob(
            @PathVariable String id,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<JobResponseDto>builder()
                .success(true)
                .message("Job closed successfully!")
                .data(jobService.closeJob(id, authentication))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('FILL_JOBS')")
    @PatchMapping("/{id}/filled")
    public ResponseEntity<ApiResponse<JobResponseDto>> fillJob(
            @PathVariable String id,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<JobResponseDto>builder()
                .success(true)
                .message("Job filled successfully!")
                .data(jobService.fillJob(id, authentication))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('ARCHIVE_JOBS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> archiveJob(
            @PathVariable String id,
            Authentication authentication) {
        jobService.archiveJob(id, authentication);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Job archived successfully!")
                .data(null)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('UNARCHIVED_JOBS')")
    @PatchMapping("/{id}/unarchived")
    public ResponseEntity<ApiResponse<String>> unarchivedJob(
            @PathVariable String id,
            Authentication authentication) {
        jobService.unarchivedJob(id, authentication);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Job unarchived successfully!")
                .data("Job unarchived successfully!")
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }
}
