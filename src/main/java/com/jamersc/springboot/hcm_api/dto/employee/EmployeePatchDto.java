package com.jamersc.springboot.hcm_api.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePatchDto {
    private String firstName;
    private String lastName;
    @Email(message = "Email should be valid")
    private String email;
//    private Long jobId;
    private String jobId;        // Hash ID of the Job
    private LocalDate hiredDate;
    @Min(value = 0, message = "Salary must be a non-negative value")
    private Double salary;
}
