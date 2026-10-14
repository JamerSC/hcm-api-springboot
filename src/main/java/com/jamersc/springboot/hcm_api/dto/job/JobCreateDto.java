package com.jamersc.springboot.hcm_api.dto.job;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobCreateDto {
    @NotBlank(message = "Job title is required")
    private String title;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Requirements; is required")
    private String requirements;
    @NotBlank(message = "Location is required")
    private String location;
    private LocalDate postedDate;
    @NotBlank(message = "Department is required")
    private String departmentId; // Hash ID
}
