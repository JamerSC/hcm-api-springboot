package com.jamersc.springboot.hcm_api.service.application;

import com.jamersc.springboot.hcm_api.dto.application.ApplicationResponseDto;
import com.jamersc.springboot.hcm_api.dto.application.ApplicationUpdateDto;
import com.jamersc.springboot.hcm_api.entity.*;
import com.jamersc.springboot.hcm_api.mapper.ApplicationMapper;
import com.jamersc.springboot.hcm_api.repository.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ApplicationMapper applicationMapper, UserRepository userRepository, RoleRepository roleRepository, EmployeeRepository employeeRepository, JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
    }

//    @Override
//    public List<ApplicationResponseDto> getAllApplication() {
//        return applicationMapper.entitiesToResponseDtos(
//                applicationRepository.findAll()
//        );
//    }

    @Override
    public Page<ApplicationResponseDto> getAllApplications(Pageable pageable) {
        // fetch application
        Page<Application> applications = applicationRepository.findAll(pageable);
        // map application entity page to page dto
        return applications.map(applicationMapper::entityToApplicationResponseDto);
    }

    @Override
    public Optional<ApplicationResponseDto> getApplication(Long id) {
        return Optional.ofNullable(applicationRepository.findById(id)
                .map(applicationMapper::entityToApplicationResponseDto)
                .orElseThrow(()-> new RuntimeException("Application not found")));
    }

    @Override
    public ApplicationResponseDto updateApplicationInformation(Long id, ApplicationUpdateDto dto, Authentication authentication) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Application not found"));
        User currentUser = getUser(authentication);

        // 3. Update only the fields provided in DTO (non-null updates)
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

        // 4. Set updater info
        application.setUpdatedBy(currentUser);
        application.setUpdatedAt(OffsetDateTime.now());

        // 5. Save updated entity
        Application updatedApplication = applicationRepository.save(application);

        // 6. Return mapped response
        return applicationMapper.entityToApplicationResponseDto(updatedApplication);
    }

    @Override
    public ApplicationResponseDto initialQualification(Long id, Authentication authentication) {
        // find application id
        Application application = applicationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Application not found"));

        // authenticate user & find by username
        User currentUser = getUser(authentication);

        // set the application status & updated by
        application.setStatus(ApplicationStatus.INITIAL_QUALIFICATION);
        application.setUpdatedBy(currentUser);

        // update application status
        Application initialQualification = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(initialQualification);
    }

    @Override
    public ApplicationResponseDto firstInterview(Long id, Authentication authentication) {
        // find application id
        Application application = applicationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Application not found"));

        // authenticate user & find by username
        User currentUser = getUser(authentication);

        // set the application status & updated by
        application.setStatus(ApplicationStatus.FIRST_INTERVIEW);
        application.setUpdatedBy(currentUser);

        // update application status
        Application firstInterview = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(firstInterview);
    }

    @Override
    public ApplicationResponseDto secondInterview(Long id, Authentication authentication) {
        // find application id
        Application application = applicationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Application not found"));

        // authenticate user & find by username
        User currentUser = getUser(authentication);

        // set the application status & updated by
        application.setStatus(ApplicationStatus.SECOND_INTERVIEW);
        application.setUpdatedBy(currentUser);

        // update application status
        Application secondInterview = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(secondInterview);
    }

    @Override
    public ApplicationResponseDto contractProposal(Long id, Authentication authentication) {
        // find application id
        Application application = applicationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Application not found"));

        // authenticate user & find by username
        User currentUser = getUser(authentication);

        // set the application status & updated by
        application.setStatus(ApplicationStatus.CONTRACT_PROPOSAL);
        application.setUpdatedBy(currentUser);

        // update application status
        Application contractProposal = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(contractProposal);
    }

    @Override
    public ApplicationResponseDto contractSigned(Long id, Authentication authentication) {
        // find application id
        Application application = applicationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Application not found"));

        // authenticate user & find by username
        User currentUser = getUser(authentication);

        // set the application status & updated by
        application.setStatus(ApplicationStatus.CONTRACT_SIGNED);
        application.setUpdatedBy(currentUser);

        // update application status
        Application contractSigned = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(contractSigned);
    }

    @Override
    public ApplicationResponseDto approveApplication(Long id, Authentication authentication) {
        // find application id
        Application application = applicationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Application not found"));

        // authenticate user & find by username
        User currentUser = getUser(authentication);

        // set the application status & updated by
        application.setStatus(ApplicationStatus.APPROVED);
        application.setUpdatedBy(currentUser);

        // update application status
        Application approvedApplication = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(approvedApplication);
    }

    @Override
    public ApplicationResponseDto rejectApplication(Long id, Authentication authentication) {
        // find application id
        Application application = applicationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Application not found"));

        // authenticate user & find by username
        User currentUser = getUser(authentication);

        // set the application status & updated by
        application.setStatus(ApplicationStatus.REJECTED);
        application.setUpdatedBy(currentUser);

        // update application status
        Application rejectedApplication = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(rejectedApplication);
    }

    @Override
    public ApplicationResponseDto hireApplication(Long id, Authentication authentication) {
        // check application
        Application application = applicationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Application not found"));

        // authenticate user & find by username
        User currentUser = getUser(authentication);

        if (application.getStatus() != ApplicationStatus.CONTRACT_SIGNED) {
            throw new RuntimeException("Cannot hire applicant. Application status is not 'Contract Signed'.");
        }

        /** set the application status & updated by */
        application.setStatus(ApplicationStatus.HIRED);
        application.setUpdatedBy(currentUser);

        /** get the user & applicant entities ***/
        User user = application.getApplicant().getUser(); // get user email
        Applicant applicant = application.getApplicant();  // get applicant info

        Employee newEmployee = new Employee();
        newEmployee.setFirstName(applicant.getFirstName());
        newEmployee.setLastName(applicant.getLastName());
        newEmployee.setEmail(user.getEmail());
        newEmployee.setHiredDate(LocalDate.now()); // Local date now temporary

        /** set job */
        Job job = jobRepository.findById(application.getJob().getId())
                .orElseThrow(() -> new RuntimeException("Job not found!"));
        newEmployee.setJob(job);

        newEmployee.setCreatedBy(currentUser);
        newEmployee.setUpdatedBy(currentUser);

        // save new employee entity
        Employee employee = employeeRepository.save(newEmployee);

//        // auto create employee contract
//        Contract contract = new Contract();
//        contract.setEmployee(employee);
//        contract.setCreatedBy(currentUser);
//        contract.setUpdatedBy(currentUser);
//        contract.setSalary(application.getProposedSalary());
//        contract.setTitle(application.getJob().getTitle());
//        // other fields to populate

        // update application status
        Application hireApplicant = applicationRepository.save(application);
        return applicationMapper.entityToApplicationResponseDto(hireApplicant);
    }

    // Authenticate the user - extracted method
    private User getUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in the database!"));
    }
}
