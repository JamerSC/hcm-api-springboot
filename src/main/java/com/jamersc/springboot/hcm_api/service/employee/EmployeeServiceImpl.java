package com.jamersc.springboot.hcm_api.service.employee;

import com.jamersc.springboot.hcm_api.dto.employee.*;
import com.jamersc.springboot.hcm_api.entity.Job;
import com.jamersc.springboot.hcm_api.entity.User;
import com.jamersc.springboot.hcm_api.exception.EmployeeNotFoundException;
import com.jamersc.springboot.hcm_api.entity.Employee;
import com.jamersc.springboot.hcm_api.mapper.EmployeeMapper;
import com.jamersc.springboot.hcm_api.repository.EmployeeRepository;
import com.jamersc.springboot.hcm_api.repository.EmployeeSpecification;
import com.jamersc.springboot.hcm_api.repository.JobRepository;
import com.jamersc.springboot.hcm_api.repository.UserRepository;
import com.jamersc.springboot.hcm_api.service.security.HashidService;
import com.jamersc.springboot.hcm_api.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserService userService;
    private final JobRepository jobRepository;
    private final HashidService hashService;

    @Override
    public Page<EmployeeResponseDto> getAllEmployees(
            String search,
            String jobId,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    ) {
        Long decodedJobId = (jobId != null && !jobId.isBlank()) ? hashService.decode(jobId) : null;

        Specification<Employee> spec = Specification.allOf(
                EmployeeSpecification.search(search),
                EmployeeSpecification.hasJob(decodedJobId),
                EmployeeSpecification.dateRange(dateFrom, dateTo)
        );

        return employeeRepository.findAll(spec, pageable)
                .map(employeeMapper::entityToEmployeeResponseDTO);
    }

    @Override
    public Optional<EmployeeProfileDto> getEmployeeProfile(String id) {
        Long rawId = hashService.decode(id);
        return Optional.of(employeeRepository.findEmployeeWithUserAndRolesById(rawId)
                .map(employeeMapper::entityToProfileDto)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id)));
    }

    @Override
    public Optional<EmployeeResponseDto> getEmployee(String id) {
        Long rawId = hashService.decode(id);
        return Optional.of(employeeRepository.findById(rawId)
                .map(employeeMapper::entityToEmployeeResponseDTO)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id)));
    }

    @Override
    public Optional<EmployeeDto> findByEmployee(String id) {
        Long rawId = hashService.decode(id);
        return Optional.of(employeeRepository.findById(rawId)
                .map(employeeMapper::entityToDto)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id)));
    }

    @Override
    public EmployeeProfileDto getMyEmployeeProfile(Authentication authentication) {
        User userDetails = userService.getUserAuthentication(authentication);
        Employee myProfile = employeeRepository.findEmployeeByUsername(userDetails.getUsername());
        return employeeMapper.entityToProfileDto(myProfile);
    }

    @Override
    public EmployeeResponseDto createEmployee(EmployeeCreateDto dto, Authentication authentication) {
        User currentUser = userService.getUserAuthentication(authentication);

        Long decodedJobId = hashService.decode(dto.getJobId());
        Job job = jobRepository.findById(decodedJobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + dto.getJobId()));

        Employee newEmployee = employeeMapper.createDtoToEntity(dto);
        newEmployee.setJob(job);
        newEmployee.setCreatedBy(currentUser);
        newEmployee.setUpdatedBy(currentUser);

        Employee createdEmployee = employeeRepository.save(newEmployee);
        return employeeMapper.entityToEmployeeResponseDTO(createdEmployee);
    }

    @Override
    public EmployeeResponseDto patchEmployee(String id, EmployeePatchDto dto, Authentication authentication) {
        User currentUser = userService.getUserAuthentication(authentication);
        Long rawId = hashService.decode(id);

        Employee employee = employeeRepository.findById(rawId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        if (dto.getFirstName() != null && !dto.getFirstName().isBlank()) {
            employee.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null && !dto.getLastName().isBlank()) {
            employee.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            employee.setEmail(dto.getEmail());
        }
        if (dto.getJobId() != null && !dto.getJobId().isBlank()) {
            Long decodedJobId = hashService.decode(dto.getJobId());
            Job job = jobRepository.findById(decodedJobId)
                    .orElseThrow(() -> new RuntimeException("Job not found with id: " + dto.getJobId()));
            employee.setJob(job);
        }
        if (dto.getHiredDate() != null) {
            employee.setHiredDate(dto.getHiredDate());
        }
        if (dto.getSalary() != null) {
            employee.setSalary(dto.getSalary());
        }
        employee.setUpdatedBy(currentUser);

        Employee patchedEmployee = employeeRepository.save(employee);
        return employeeMapper.entityToEmployeeResponseDTO(patchedEmployee);
    }

    @Override
    public void archiveEmployee(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        if (!employeeRepository.existsById(rawId)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(rawId);
    }

    @Override
    public EmployeeResponseDto unarchivedEmployee(String id, Authentication authentication) {
        return null;
    }

}
