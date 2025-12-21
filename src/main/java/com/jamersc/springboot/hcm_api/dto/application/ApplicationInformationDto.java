package com.jamersc.springboot.hcm_api.dto.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jamersc.springboot.hcm_api.entity.ApplicationStatus;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ApplicationInformationDto {

    private Long id;

    // Applicant Info
    private String applicantName;
    private String email;
    private String phone;
    private String mobile;
    private String linkedInProfile;

    // Job Info
    private String jobPosition;
    private String jobDescription;

    // Application Info
    private final ApplicationStatus status = ApplicationStatus.NEW;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime appliedAt;

    // Contract / Offer Info
    private Double expectedSalary;
    private Double proposedSalary;

    // Additional Information
    private String degree;
    private String source;
    private Date availability;
    private String applicationSummary;
    private String skills;
    private final Set<String> employees = new HashSet<>();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime createdAt;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime updatedAt;

    private String updatedBy;
}
