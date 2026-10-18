package com.jamersc.springboot.hcm_api.service.applicant;

import com.jamersc.springboot.hcm_api.dto.applicant.ApplicantProfileDto;
import com.jamersc.springboot.hcm_api.dto.applicant.ApplicantResponseDto;
import com.jamersc.springboot.hcm_api.dto.application.ApplicationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

public interface ApplicantService {

    Page<ApplicantResponseDto> getAllApplicants(
            String search,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    );

    Optional<ApplicantResponseDto> getApplicant(String id); // Hash ID

    ApplicantResponseDto getMyApplicantProfile(Authentication authentication);

    ApplicantResponseDto updateMyApplicantProfile(ApplicantProfileDto profileDto, Authentication authentication);

    ApplicantResponseDto uploadResume(MultipartFile file, Authentication authentication);

    ApplicationResponseDto apply(String jobId, Authentication authentication); // Job Hash ID

    Page<ApplicationResponseDto> getAllApplicantJobsApplied(Pageable pageable, Authentication authentication);

    Optional<ApplicationResponseDto> getApplicantJobsApplied(String applicationId, Authentication authentication); // Application Hash ID

    ApplicationResponseDto withdrawApplication(String applicationId, Authentication authentication); // Application Hash ID

    void deleteApplicantAccount(Authentication authentication);
}
