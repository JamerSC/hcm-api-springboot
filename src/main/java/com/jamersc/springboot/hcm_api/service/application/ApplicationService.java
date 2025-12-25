package com.jamersc.springboot.hcm_api.service.application;

import com.jamersc.springboot.hcm_api.dto.application.ApplicationResponseDto;
import com.jamersc.springboot.hcm_api.dto.application.ApplicationUpdateDto;
import com.jamersc.springboot.hcm_api.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Optional;

public interface ApplicationService {
    Page<ApplicationResponseDto> getAllApplications(
            String search,
            ApplicationStatus status,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    );
    Optional<ApplicationResponseDto> getApplication(Long id);
    ApplicationResponseDto updateApplicationInformation(Long id, ApplicationUpdateDto dto, Authentication authentication);
    ApplicationResponseDto initialQualification(Long id, Authentication authentication);
    ApplicationResponseDto firstInterview(Long id, Authentication authentication);
    ApplicationResponseDto secondInterview(Long id, Authentication authentication);
    ApplicationResponseDto contractProposal(Long id, Authentication authentication);
    ApplicationResponseDto contractSigned(Long id, Authentication authentication);
    ApplicationResponseDto approveApplication(Long id, Authentication authentication);
    ApplicationResponseDto rejectApplication(Long id, Authentication authentication);
    ApplicationResponseDto hireApplication(Long id, Authentication authentication);
}
