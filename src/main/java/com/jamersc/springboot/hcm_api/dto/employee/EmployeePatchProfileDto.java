package com.jamersc.springboot.hcm_api.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePatchProfileDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String jobPosition;
    private String department;
    private LocalDate hiredDate;
    private Double salary;
    private String username;
    private Set<String> roles;
}
