package com.jamersc.springboot.hcm_api.service.application;

import com.jamersc.springboot.hcm_api.dto.application.ApplicationResponseDto;
import com.jamersc.springboot.hcm_api.dto.application.ApplicationUpdateDto;
import com.jamersc.springboot.hcm_api.entity.*;
import com.jamersc.springboot.hcm_api.mapper.ApplicationMapper;
import com.jamersc.springboot.hcm_api.repository.*;
import com.jamersc.springboot.hcm_api.service.security.HashidService;
import com.jamersc.springboot.hcm_api.service.user.UserService;
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
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final UserService userService;
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private final HashidService hashService;

    @Override
    public Page<ApplicationResponseDto> getAllApplications(
            String search,
            ApplicationStatus status,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    ) {
        log.debug("Fetching all applications filters: search={}, status={}, created date from {}, date to {}, pageable={}",
                search, status, dateFrom, dateTo, pageable);

        Specification<Application> spec = Specification.allOf(
                ApplicationSpecification.search(search),
                ApplicationSpecification.hasStatus(status),
                ApplicationSpecification.dateRange(dateFrom, dateTo)
        );

        return applicationRepository.findAll(spec, pageable)
                .map(applicationMapper::entityToApplicationResponseDto);
    }

    @Override
    public Optional<ApplicationResponseDto> getApplication(String id) {
        Long rawId = hashService.decode(id);
        return Optional.of(applicationRepository.findById(rawId)
                .map(applicationMapper::entityToApplicationResponseDto)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id)));
    }

    @Override
    public ApplicationResponseDto updateApplicationInformation(String id, ApplicationUpdateDto dto, Authentication authentication) {
        Long rawId = hashService.decode(id);
        Application application = applicationRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
        User currentUser = userService.getUserAuthentication(authentication);

        if (dto.getEmail() != null) application.setEmail(dto.getEmail());
        if (dto.getPhone() != null) application.setPhone(dto.getPhone());
        if (dto.getMobile() != null) application.setMobile(dto.getMobile());
        if (dto.getLinkedInProfile() != null) application.setLinkedInProfile(dto.getLinkedInProfile());
        if (dto.getDegree() != null) application.setDegree(dto.getDegree());
        if (dto.getEmployees() != null && !dto.getEmployees().isEmpty()) application.setEmployees(dto.getEmployees());
        if (dto.getSource() != null) application.setSource(dto.getSource());
        if (dto.getAvailability() != null) application.setAvailability(dto.getAvailability());
        if (dto.getExpectedSalary() != null) application.setExpectedSalary(dto.getExpectedSalary());
        if (dto.getProposedSalary() != null) application.setProposedSalary(dto.getProposedSalary());
        if (dto.getApplicationSummary() != null) application.setApplicationSummary(dto.getApplicationSummary());
        if (dto.getSkills() != null) application.setSkills(dto.getSkills());
        if (dto.getStatus() != null) application.setStatus(dto.getStatus());

        application.setUpdatedBy(currentUser);
        application.setUpdatedAt(OffsetDateTime.now());

        Application updatedApplication = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(updatedApplication);
    }

    @Override
    public ApplicationResponseDto initialQualification(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        Application application = applicationRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));

        User currentUser = userService.getUserAuthentication(authentication);
        application.setStatus(ApplicationStatus.INITIAL_QUALIFICATION);
        application.setUpdatedBy(currentUser);

        Application updated = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(updated);
    }

    @Override
    public ApplicationResponseDto firstInterview(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        Application application = applicationRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));

        User currentUser = userService.getUserAuthentication(authentication);
        application.setStatus(ApplicationStatus.FIRST_INTERVIEW);
        application.setUpdatedBy(currentUser);

        Application updated = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(updated);
    }

    @Override
    public ApplicationResponseDto secondInterview(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        Application application = applicationRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));

        User currentUser = userService.getUserAuthentication(authentication);
        application.setStatus(ApplicationStatus.SECOND_INTERVIEW);
        application.setUpdatedBy(currentUser);

        Application updated = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(updated);
    }

    @Override
    public ApplicationResponseDto contractProposal(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        Application application = applicationRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));

        User currentUser = userService.getUserAuthentication(authentication);
        application.setStatus(ApplicationStatus.CONTRACT_PROPOSAL);
        application.setUpdatedBy(currentUser);

        Application updated = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(updated);
    }

    @Override
    public ApplicationResponseDto contractSigned(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        Application application = applicationRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));

        User currentUser = userService.getUserAuthentication(authentication);
        application.setStatus(ApplicationStatus.CONTRACT_SIGNED);
        application.setUpdatedBy(currentUser);

        Application updated = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(updated);
    }

    @Override
    public ApplicationResponseDto approveApplication(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        Application application = applicationRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));

        User currentUser = userService.getUserAuthentication(authentication);
        application.setStatus(ApplicationStatus.APPROVED);
        application.setUpdatedBy(currentUser);

        Application updated = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(updated);
    }

    @Override
    public ApplicationResponseDto rejectApplication(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        Application application = applicationRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));

        User currentUser = userService.getUserAuthentication(authentication);
        application.setStatus(ApplicationStatus.REJECTED);
        application.setUpdatedBy(currentUser);

        Application updated = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(updated);
    }

    @Override
    public ApplicationResponseDto hireApplication(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        Application application = applicationRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));

        User currentUser = userService.getUserAuthentication(authentication);

        if (application.getStatus() != ApplicationStatus.CONTRACT_SIGNED) {
            throw new RuntimeException("Cannot hire applicant. Application status is not 'Contract Signed'.");
        }

        application.setStatus(ApplicationStatus.HIRED);
        application.setUpdatedBy(currentUser);

        User user = application.getApplicant().getUser();
        Applicant applicant = application.getApplicant();

        Employee newEmployee = new Employee();
        newEmployee.setFirstName(applicant.getFirstName());
        newEmployee.setLastName(applicant.getLastName());
        newEmployee.setEmail(user.getEmail());
        newEmployee.setHiredDate(LocalDate.now());

        Job job = jobRepository.findById(application.getJob().getId())
                .orElseThrow(() -> new RuntimeException("Job not found!"));
        newEmployee.setJob(job);
        newEmployee.setCreatedBy(currentUser);
        newEmployee.setUpdatedBy(currentUser);

        employeeRepository.save(newEmployee);

        Application hired = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(hired);
    }
}
