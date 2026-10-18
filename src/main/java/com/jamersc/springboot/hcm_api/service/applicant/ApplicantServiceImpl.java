package com.jamersc.springboot.hcm_api.service.applicant;

import com.jamersc.springboot.hcm_api.dto.applicant.ApplicantProfileDto;
import com.jamersc.springboot.hcm_api.dto.applicant.ApplicantResponseDto;
import com.jamersc.springboot.hcm_api.dto.application.ApplicationResponseDto;
import com.jamersc.springboot.hcm_api.entity.*;
import com.jamersc.springboot.hcm_api.mapper.ApplicantMapper;
import com.jamersc.springboot.hcm_api.mapper.ApplicationMapper;
import com.jamersc.springboot.hcm_api.repository.*;
import com.jamersc.springboot.hcm_api.service.email.EmailService;
import com.jamersc.springboot.hcm_api.service.file.FileStorageService;
import com.jamersc.springboot.hcm_api.service.security.HashidService;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ApplicantServiceImpl implements ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicantMapper applicantMapper;
    private final ApplicationMapper applicationMapper;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;
    private final HashidService hashService;

    @Override
    public Page<ApplicantResponseDto> getAllApplicants(
            String search,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    ) {
        log.debug("Fetching all applicants filters: search={}, created date from {}, date to {}, pageable={}",
                search, dateFrom, dateTo, pageable
        );

        Specification<Applicant> spec = Specification.allOf(
                ApplicantSpecification.search(search),
                ApplicantSpecification.dateRange(dateFrom, dateTo)
        );

        return applicantRepository.findAll(spec, pageable)
                .map(applicantMapper::entityToResponseDto);
    }

    @Override
    public Optional<ApplicantResponseDto> getApplicant(String id) {
        Long rawId = hashService.decode(id);
        return Optional.of(applicantRepository.findById(rawId)
                .map(applicantMapper::entityToResponseDto)
                .orElseThrow(() -> new RuntimeException("Applicant not found with id: " + id)));
    }

    @Override
    public ApplicantResponseDto getMyApplicantProfile(Authentication authentication) {
        User currentUser = getUser(authentication);
        Applicant myProfile = applicantRepository.findApplicantByUsername(currentUser.getUsername());
        return applicantMapper.entityToResponseDto(myProfile);
    }

    @Override
    public ApplicantResponseDto updateMyApplicantProfile(ApplicantProfileDto profileDto, Authentication authentication) {
        User currentUser = getUser(authentication);

        Applicant existingApplicant = applicantRepository.findByApplicantUser(currentUser)
                .orElseThrow(() -> {
                    log.error("Applicant user not found - {}", currentUser);
                    return new RuntimeException("Applicant user not found");
                });

        existingApplicant.setFirstName(profileDto.getFirstName());
        existingApplicant.setLastName(profileDto.getLastName());
        existingApplicant.setPhoneNumber(profileDto.getPhoneNumber());
        existingApplicant.setAddress(profileDto.getAddress());
        existingApplicant.setEducationLevel(profileDto.getEducationLevel());
        existingApplicant.setUpdatedBy(currentUser);

        Applicant updatedApplicant = applicantRepository.save(existingApplicant);
        return applicantMapper.entityToResponseDto(updatedApplicant);
    }

    @Override
    public ApplicantResponseDto uploadResume(MultipartFile file, Authentication authentication) {
        User applicantUser = getUser(authentication);
        Applicant applicant = applicantRepository.findByApplicantUser(applicantUser)
                .orElseThrow(() -> new RuntimeException("Applicant profile not found for user: " + applicantUser));

        String storeFileName = fileStorageService.storeFile(file, String.valueOf(applicantUser.getId()));

        applicant.setCvFilePath(storeFileName);
        applicant.setUpdatedBy(applicantUser);

        Applicant uploadResume = applicantRepository.save(applicant);
        return applicantMapper.entityToResponseDto(uploadResume);
    }

    @Override
    public ApplicationResponseDto apply(String jobId, Authentication authentication) {
        Long rawJobId = hashService.decode(jobId);
        Job job = jobRepository.findById(rawJobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        User applicantUser = getUser(authentication);
        Applicant applicant = applicantRepository.findByApplicantUser(applicantUser)
                .orElseThrow(() -> new RuntimeException("Applicant profile not found!"));

        if (applicationRepository.findByApplicantAndJob(applicant, job).isPresent()) {
            throw new RuntimeException("You have already applied for this job.");
        }

        Application newApplication = new Application();
        newApplication.setApplicant(applicant);
        newApplication.setJob(job);
        newApplication.setUpdatedBy(applicantUser);

        Application submittedApplication = applicationRepository.save(newApplication);

        String email = submittedApplication.getApplicant().getUser().getEmail();
        String fullName = submittedApplication.getApplicant().getApplicantFullName();
        String jobTitle = submittedApplication.getJob().getTitle();
        long applicationId = submittedApplication.getId();

        emailService.sendSubmittedApplicationEmail(email, fullName, applicationId, jobTitle);

        return applicationMapper.entityToApplicationResponseDto(submittedApplication);
    }

    @Override
    public Page<ApplicationResponseDto> getAllApplicantJobsApplied(Pageable pageable, Authentication authentication) {
        User applicantUser = getUser(authentication);
        Applicant applicant = applicantRepository.findByApplicantUser(applicantUser)
                .orElseThrow(() -> new RuntimeException("Applicant profile not found."));

        Page<Application> applications = applicationRepository.findApplicantApplicationsById(pageable, applicant.getId());
        return applications.map(applicationMapper::entityToApplicationResponseDto);
    }

    @Override
    public Optional<ApplicationResponseDto> getApplicantJobsApplied(String applicationId, Authentication authentication) {
        Long rawApplicationId = hashService.decode(applicationId);
        Application application = applicationRepository.findById(rawApplicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

        User applicantUser = getUser(authentication);
        Applicant applicant = applicantRepository.findByApplicantUser(applicantUser)
                .orElseThrow(() -> new RuntimeException("Applicant profile not found."));

        return Optional.ofNullable(
                applicationRepository.findByApplicantAndId(applicant, application.getId())
                        .map(applicationMapper::entityToApplicationResponseDto)
                        .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId)));
    }

    @Override
    public ApplicationResponseDto withdrawApplication(String applicationId, Authentication authentication) {
        Long rawApplicationId = hashService.decode(applicationId);
        Application application = applicationRepository.findById(rawApplicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

        User applicantUser = getUser(authentication);

        application.setStatus(ApplicationStatus.WITHDRAWN);
        application.setUpdatedBy(applicantUser);
        Application withdrawnApplication = applicationRepository.save(application);

        return applicationMapper.entityToApplicationResponseDto(withdrawnApplication);
    }

    @Override
    public void deleteApplicantAccount(Authentication authentication) {
        User userDetails = getUser(authentication);
        User userToDelete = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found."));

        Applicant applicantProfile = applicantRepository.findByApplicantUser(userToDelete)
                .orElseThrow(() -> new RuntimeException("Applicant profile not found."));

        List<Application> applications = applicationRepository.findByApplicant(applicantProfile);
        applicationRepository.deleteAll(applications);

        userRepository.delete(userToDelete);
    }

    private User getUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found."));
    }
}
