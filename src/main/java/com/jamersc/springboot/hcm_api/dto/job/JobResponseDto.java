package com.jamersc.springboot.hcm_api.dto.job;

import com.jamersc.springboot.hcm_api.entity.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobResponseDto {
    private String jobId;
    private LocalDate postedDate;
    private String postedBy;
    private String title;
    private String department;
    private String description;
    private String location;
    private JobStatus status;
    private String requirements;
}
