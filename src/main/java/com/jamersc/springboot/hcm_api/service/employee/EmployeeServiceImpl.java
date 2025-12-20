package com.jamersc.springboot.hcm_api.service.employee;

import com.jamersc.springboot.hcm_api.dto.employee.*;
import com.jamersc.springboot.hcm_api.entity.Job;
import com.jamersc.springboot.hcm_api.entity.User;
import com.jamersc.springboot.hcm_api.exception.EmployeeNotFoundException;
import com.jamersc.springboot.hcm_api.entity.Employee;
import com.jamersc.springboot.hcm_api.mapper.EmployeeMapper;
import com.jamersc.springboot.hcm_api.repository.EmployeeRepository;
import com.jamersc.springboot.hcm_api.repository.JobRepository;
import com.jamersc.springboot.hcm_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, UserRepository userRepository, JobRepository jobRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }


    @Override
    public Page<EmployeeResponseDto> getAllEmployee(Pageable pageable) {
        Page<Employee> employees = employeeRepository.findAll(pageable);
        return employees.map(employeeMapper::entityToEmployeeResponseDTO);
    }

    @Override
    public Optional<EmployeeProfileDto> getEmployeeProfile(Long id) {
       return Optional.ofNullable(employeeRepository.findEmployeeWithUserAndRolesById(id)
                .map(employeeMapper::entityToProfileDto).orElseThrow(
                        () -> new EmployeeNotFoundException("Employee id not found - " + id))
       );
    }

    public Optional<EmployeeResponseDto> getEmployee(Long id) {
        return Optional.ofNullable(employeeRepository.findById(id)
                .map(employeeMapper::entityToEmployeeResponseDTO).orElseThrow(
                        () -> new EmployeeNotFoundException("Employee id not found - " + id))
        );
    }

    public Optional<EmployeeDto> findById(Long id) {
        return Optional.ofNullable(employeeRepository.findById(id)
                .map(employeeMapper::entityToDto).orElseThrow(
                        () -> new EmployeeNotFoundException("Employee id not found - " + id))
        );
    }

    @Override
    public EmployeeProfileDto getMyEmployeeProfile(Authentication authentication) {
        User userDetails = getUser(authentication);
        Employee myProfile = employeeRepository.findEmployeeByUsername(userDetails.getUsername());
        return employeeMapper.entityToProfileDto(myProfile);
    }

    @Override
    public EmployeeResponseDto createEmployee(EmployeeCreateDto dto, Authentication authentication) {
        // get current user
        User currentUser = getUser(authentication);

        // check if job exist
        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> {
                    log.error("Job not found with ID: {}", dto.getJobId());
                    return new RuntimeException("Job not found with id: " + dto.getJobId());
                });

        // map dto to entity
        Employee newEmployee = employeeMapper.createDtoToEntity(dto);
        newEmployee.setJob(job);
        newEmployee.setCreatedBy(currentUser);
        newEmployee.setUpdatedBy(currentUser);

        // save the entity using the repository
        Employee createdEmployee = employeeRepository.save(newEmployee);

        // map employee entity to response emp dto
        return employeeMapper.entityToEmployeeResponseDTO(createdEmployee);
    }

    @Override
    public Employee updateEmployee(EmployeeUpdateDto dto, Authentication authentication) {
        // get current user
        User currentUser = getUser(authentication);

        // Convert to entity
        Employee employee = employeeMapper.updateDtoToEntity(dto);

        // set updated by current user
        employee.setUpdatedBy(currentUser);

        // Update employee
        return employeeRepository.save(employee);
    }

    @Override
    public EmployeeResponseDto patchEmployee(Long id, EmployeePatchDto dto, Authentication authentication) {
        User currentUser = getUser(authentication);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Employee not found"));

        if (dto.getFirstName() != null && !dto.getFirstName().isBlank()) {
            employee.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null && !dto.getLastName().isBlank()) {
            employee.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            employee.setEmail(dto.getEmail());
        }
        if (dto.getJobId() != null) {
            Job job = jobRepository.findById(dto.getJobId())
                    .orElseThrow(() -> new RuntimeException("Job not found"));
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
    public void archiveEmployee(Long id, Authentication authentication) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found - " + id));
        employeeRepository.deleteById(id);
    }

    @Override
    public EmployeeResponseDto unarchivedEmployee(Long id, Authentication authentication) {
        return null;
    }

    // method for getting the current user - reusability
    private User getUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user is not found!"));
    }
}
