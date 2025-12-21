package com.jamersc.springboot.hcm_api.dto.application;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.jamersc.springboot.hcm_api.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUpdateDto {
    private Long applicationId;
    private String applicantName;
    private String appliedPosition;
    private String description;
    private ApplicationStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime appliedAt;

    // other subject information
    private String email;
    private String phone;
    private String mobile;
    private String linkedInProfile; // link or name text field
    private String degree; // degree table college, associate, masters single selection - cms
    private Set<String> employees = new HashSet<>(); // arrays of employees
    private String source; // create source fb, linkedin, single selection - cms
    private Date availability; // date picker
    private Double expectedSalary;
    private Double proposedSalary;
    private String applicationSummary;
    private String skills; // it should be a cms of skills

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime createdAt;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime updatedAt;

    private String updatedBy;
}
