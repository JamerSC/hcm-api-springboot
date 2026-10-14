package com.jamersc.springboot.hcm_api.service.job;

import com.jamersc.springboot.hcm_api.dto.job.JobCreateDto;
import com.jamersc.springboot.hcm_api.dto.job.JobDto;
import com.jamersc.springboot.hcm_api.dto.job.JobPatchDto;
import com.jamersc.springboot.hcm_api.dto.job.JobResponseDto;
import com.jamersc.springboot.hcm_api.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Optional;

public interface JobService {
    Page<JobDto> getAllJob(
            String search,
            String departmentId, // Hash ID
            JobStatus status,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    );
    Page<JobResponseDto> getOpenJobs(
            String search,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    );
    Optional<JobResponseDto> getJob(String id);
    JobResponseDto createJob(JobCreateDto dto, Authentication authentication);
    JobResponseDto openJob(String id, Authentication authentication);
    JobResponseDto updateJob(String id, JobPatchDto dto, Authentication authentication);
    JobResponseDto fillJob(String id, Authentication authentication);
    JobResponseDto closeJob(String id, Authentication authentication);
    void archiveJob(String id, Authentication authentication);
    void unarchivedJob(String id, Authentication authentication);
}
