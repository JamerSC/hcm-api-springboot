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
            Long departmentId,
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
    Optional<JobResponseDto> getJob(Long id);
    JobResponseDto createJob(JobCreateDto dto, Authentication authentication);
    JobResponseDto openJob(Long id, Authentication authentication);
    JobResponseDto updateJob(Long id, JobPatchDto dto, Authentication authentication);
    JobResponseDto fillJob(Long id, Authentication authentication);
    JobResponseDto closeJob(Long id, Authentication authentication);
    void archiveJob(Long id, Authentication authentication);
    void unarchivedJob(Long id, Authentication authentication);
}
