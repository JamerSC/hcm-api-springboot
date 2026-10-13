package com.jamersc.springboot.hcm_api.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateDto {
    @NotBlank(message = "Job title is required")
    private String jobId;        // Hash ID of the Job
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    @NotNull(message = "Hired date is required")
    private LocalDate hiredDate;
    @NotNull(message = "Salary is required")
    @Min(value = 0, message = "Salary must be a non-negative value")
    private Double salary;
}