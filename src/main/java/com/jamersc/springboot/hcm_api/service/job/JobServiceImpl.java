package com.jamersc.springboot.hcm_api.service.job;

import com.jamersc.springboot.hcm_api.dto.job.JobCreateDto;
import com.jamersc.springboot.hcm_api.dto.job.JobDto;
import com.jamersc.springboot.hcm_api.dto.job.JobPatchDto;
import com.jamersc.springboot.hcm_api.dto.job.JobResponseDto;
import com.jamersc.springboot.hcm_api.entity.Department;
import com.jamersc.springboot.hcm_api.entity.Job;
import com.jamersc.springboot.hcm_api.entity.JobStatus;
import com.jamersc.springboot.hcm_api.entity.User;
import com.jamersc.springboot.hcm_api.mapper.JobMapper;
import com.jamersc.springboot.hcm_api.repository.DepartmentRepository;
import com.jamersc.springboot.hcm_api.repository.JobRepository;
import com.jamersc.springboot.hcm_api.repository.JobSpecification;
import com.jamersc.springboot.hcm_api.repository.UserRepository;
import com.jamersc.springboot.hcm_api.service.security.HashidService;
import com.jamersc.springboot.hcm_api.service.user.UserService;
import com.jamersc.springboot.hcm_api.service.user.UserServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserService userService;
    private final JobMapper jobMapper;
    private final DepartmentRepository departmentRepository;
    private final HashidService hashService;

    @Override
    public Page<JobDto> getAllJob(
            String search,
            String departmentId,
            JobStatus status,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    ) {
        Long decodedDeptId = (departmentId != null && !departmentId.isBlank())
                ? hashService.decode(departmentId)
                : null;

        Specification<Job> spec = Specification.allOf(
                JobSpecification.search(search),
                JobSpecification.hasDepartment(decodedDeptId),
                JobSpecification.hasStatus(status),
                JobSpecification.dateRange(dateFrom, dateTo)
        );

        return jobRepository.findAll(spec, pageable).map(jobMapper::entityToJobDto);
    }

    @Override
    public Page<JobResponseDto> getOpenJobs(
            String search,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    ) {
        Specification<Job> spec = Specification.allOf(
                JobSpecification.hasStatusOpen(),
                JobSpecification.search(search),
                JobSpecification.dateRange(dateFrom, dateTo)
        );
        return jobRepository.findAll(spec, pageable).map(jobMapper::entityToJobResponseDto);
    }

    @Override
    public Optional<JobResponseDto> getJob(String id) {
        Long rawId = hashService.decode(id);
        return Optional.of(jobRepository.findById(rawId)
                        .map(jobMapper::entityToJobResponseDto))
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }

    @Override
    public JobResponseDto createJob(JobCreateDto dto, Authentication authentication) {
        Long rawDeptId = hashService.decode(dto.getDepartmentId());
        Department department = departmentRepository.findById(rawDeptId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + dto.getDepartmentId()));

        User currentUser = userService.getUserAuthentication(authentication);

        Job job = jobMapper.jobCreateDtoToEntity(dto);
        job.setDepartment(department);
        job.setStatus(JobStatus.CREATED);
        job.setCreatedBy(currentUser);
        job.setUpdatedBy(currentUser);

        Job savedJob = jobRepository.save(job);
        return jobMapper.entityToJobResponseDto(savedJob);
    }

    @Override
    public JobResponseDto openJob(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        Job job = jobRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

        User currentUser = userService.getUserAuthentication(authentication);

        job.setStatus(JobStatus.OPEN);
        job.setPostedBy(currentUser);
        job.setPostedDate(LocalDate.now());
        job.setUpdatedBy(currentUser);

        Job openJob = jobRepository.save(job);
        return jobMapper.entityToJobResponseDto(openJob);
    }

    @Override
    public JobResponseDto updateJob(String id, JobPatchDto dto, Authentication authentication) {
        Long rawId = hashService.decode(id);
        Job job = jobRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

        User currentUser = userService.getUserAuthentication(authentication);

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            job.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            job.setDescription(dto.getDescription());
        }
        if (dto.getRequirements() != null && !dto.getRequirements().isBlank()) {
            job.setRequirements(dto.getRequirements());
        }
        if (dto.getLocation() != null && !dto.getLocation().isBlank()) {
            job.setLocation(dto.getLocation());
        }
        if (dto.getDepartmentId() != null && !dto.getDepartmentId().isBlank()) {
            Long rawDeptId = hashService.decode(dto.getDepartmentId());
            Department department = departmentRepository.findById(rawDeptId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + dto.getDepartmentId()));
            job.setDepartment(department);
        }

        job.setUpdatedBy(currentUser);
        Job patchedJob = jobRepository.save(job);

        return jobMapper.entityToJobResponseDto(patchedJob);
    }

    @Override
    public JobResponseDto fillJob(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        Job job = jobRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

        User currentUser = userService.getUserAuthentication(authentication);

        job.setStatus(JobStatus.FILLED);
        job.setPostedBy(null);
        job.setUpdatedBy(currentUser);

        Job savedJob = jobRepository.save(job);
        return jobMapper.entityToJobResponseDto(savedJob);
    }

    @Override
    public JobResponseDto closeJob(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        Job job = jobRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

        User currentUser = userService.getUserAuthentication(authentication);

        job.setStatus(JobStatus.CLOSED);
        job.setPostedBy(null);
        job.setUpdatedBy(currentUser);

        Job savedJob = jobRepository.save(job);
        return jobMapper.entityToJobResponseDto(savedJob);
    }

    @Override
    public void archiveJob(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        jobRepository.deleteById(rawId);
    }

    @Override
    public void unarchivedJob(String id, Authentication authentication) {
        // Implementation for unarchiving if soft delete is used
    }
}
