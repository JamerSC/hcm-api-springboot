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
import com.jamersc.springboot.hcm_api.service.user.UserServiceImpl;
import jakarta.transaction.Transactional;
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
public class JobServiceImpl implements JobService {

    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JobMapper jobMapper;
    private final DepartmentRepository departmentRepository;

    public JobServiceImpl(JobRepository jobRepository, UserRepository userRepository, JobMapper jobMapper, DepartmentRepository departmentRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.jobMapper = jobMapper;
        this.departmentRepository = departmentRepository;
    }

    /**
     * For Admin/Manager
     */
    @Override
    public Page<JobDto> getAllJob(
            String search,
            Long departmentId,
            JobStatus status,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    ) {
        log.debug("Fetching all jobs with filters - Search: {}, department id {}, job status {},  date from {}, date to {}",
                search, departmentId, status, dateFrom, dateTo
        );

        Specification<Job> spec = Specification.allOf(
                JobSpecification.search(search),
                JobSpecification.hasDepartment(departmentId),
                JobSpecification.hasStatus(status),
                JobSpecification.dateRange(dateFrom, dateTo)
        );

        Page<Job> jobs = jobRepository.findAll(spec, pageable);

        return jobs.map(jobMapper::entityToJobDto);
    }

//    @Override
//    public Page<JobDto> getAllJob(Pageable pageable) {
//        Page<Job> jobs = jobRepository.findAll(pageable);
//        return jobs.map(jobMapper::entityToJobDto);
//    }

    /**
     * For Applicant
     */
    @Override
    public Page<JobResponseDto> getOpenJobs(
            String search,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    ) {
        log.debug("Fetching open jobs with filters - Search: {}, date from {}, date to {}",
                search, dateFrom, dateTo
        );
        Specification<Job> spec = Specification.allOf(
                JobSpecification.hasStatusOpen(),
                JobSpecification.search(search),
                JobSpecification.dateRange(dateFrom, dateTo)
        );
        Page<Job> openJobs = jobRepository.findAll(spec, pageable);
        return openJobs.map(jobMapper::entityToJobResponseDto);
    }

//    @Override
//    public Page<JobResponseDto> getOpenJobs(Pageable pageable) {
//        Page<Job> openJobs = jobRepository.findByStatus(pageable, JobStatus.OPEN);
//        return openJobs.map(jobMapper::entityToJobResponseDto);
//    }

    @Override
    public Optional<JobResponseDto> getJob(Long id) {
        return Optional.of(jobRepository.findById(id)
                        .map(jobMapper::entityToJobResponseDto))
                .orElseThrow(() -> new RuntimeException("Job Id not found"));
    }

    @Override
    public JobResponseDto createJob(JobCreateDto dto, Authentication authentication) {
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department id not found!"));
        
        User currentUser = getUser(authentication);

        Job job = jobMapper.jobCreateDtoToEntity(dto);
        job.setDepartment(department);
        job.setCreatedBy(currentUser);
        job.setUpdatedBy(currentUser);

        Job saveJob = jobRepository.save(job);

        return jobMapper.entityToJobResponseDto(saveJob);
    }

    @Override
    public JobResponseDto openJob(Long id, Authentication authentication) {
        Job job = jobRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Job not found"));

        User currentUser = getUser(authentication);

        job.setStatus(JobStatus.OPEN);
        job.setPostedBy(currentUser);
        job.setUpdatedBy(currentUser);

        Job openJob = jobRepository.save(job);

        return jobMapper.entityToJobResponseDto(openJob);
    }

    @Override
    public JobResponseDto updateJob(Long id, JobPatchDto dto, Authentication authentication) {
        Job job = jobRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Job not found"));

        User currentUser = getUser(authentication);

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            job.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            job.setTitle(dto.getDescription());
        }
        if (dto.getRequirements() != null && !dto.getRequirements().isBlank()) {
            job.setTitle(dto.getRequirements());
        }
        if (dto.getLocation() != null && !dto.getLocation().isBlank()) {
            job.setTitle(dto.getRequirements());
        }
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(()-> new RuntimeException("Department not found"));
            job.setDepartment(department);
        }

        job.setUpdatedBy(currentUser);
        Job patchedJob = jobRepository.save(job);

        return jobMapper.entityToJobResponseDto(patchedJob);
    }

    @Override
    public JobResponseDto fillJob(Long id, Authentication authentication) {
        Job job = jobRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Job not found"));

        User currentUser = getUser(authentication);

        job.setStatus(JobStatus.FILLED);
        job.setPostedBy(null);
        job.setUpdatedBy(currentUser);

        Job openJob = jobRepository.save(job);

        return jobMapper.entityToJobResponseDto(openJob);
    }

    @Override
    public JobResponseDto closeJob(Long id, Authentication authentication) {
        Job job = jobRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Job not found"));

        User currentUser = getUser(authentication);

        job.setStatus(JobStatus.CLOSED);
        job.setPostedBy(null);
        job.setUpdatedBy(currentUser);

        Job openJob = jobRepository.save(job);

        return jobMapper.entityToJobResponseDto(openJob);
    }

    // todo improve validation
    @Override
    public void archiveJob(Long id, Authentication authentication) {
//        jobRepository.deleteById(id);
    }

    // todo improve validation
    @Override
    public void unarchivedJob(Long id, Authentication authentication) {
        //
    }

    private User getUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()-> new RuntimeException("Authenticated user not found."));
    }
}
