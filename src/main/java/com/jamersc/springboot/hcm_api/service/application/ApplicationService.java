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
    Optional<ApplicationResponseDto> getApplication(String id);
    ApplicationResponseDto updateApplicationInformation(String id, ApplicationUpdateDto dto, Authentication authentication);
    ApplicationResponseDto initialQualification(String id, Authentication authentication);
    ApplicationResponseDto firstInterview(String id, Authentication authentication);
    ApplicationResponseDto secondInterview(String id, Authentication authentication);
    ApplicationResponseDto contractProposal(String id, Authentication authentication);
    ApplicationResponseDto contractSigned(String id, Authentication authentication);
    ApplicationResponseDto approveApplication(String id, Authentication authentication);
    ApplicationResponseDto rejectApplication(String id, Authentication authentication);
    ApplicationResponseDto hireApplication(String id, Authentication authentication);
}
