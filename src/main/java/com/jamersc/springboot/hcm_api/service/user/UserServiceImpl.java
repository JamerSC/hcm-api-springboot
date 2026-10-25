package com.jamersc.springboot.hcm_api.service.user;

import com.jamersc.springboot.hcm_api.dto.user.UserCreateDto;
import com.jamersc.springboot.hcm_api.dto.user.UserDto;
import com.jamersc.springboot.hcm_api.dto.user.UserResponseDto;
import com.jamersc.springboot.hcm_api.entity.Employee;
import com.jamersc.springboot.hcm_api.entity.Role;
import com.jamersc.springboot.hcm_api.entity.User;
import com.jamersc.springboot.hcm_api.exception.ResourceNotFoundException;
import com.jamersc.springboot.hcm_api.mapper.UserMapper;
import com.jamersc.springboot.hcm_api.repository.EmployeeRepository;
import com.jamersc.springboot.hcm_api.repository.RoleRepository;
import com.jamersc.springboot.hcm_api.repository.UserRepository;
import com.jamersc.springboot.hcm_api.repository.UserSpecification;
import com.jamersc.springboot.hcm_api.service.security.HashidService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final HashidService hashService;

    @Override
    public Page<UserResponseDto> getAllUsers(
            String search,
            Boolean active,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    ) {
        log.debug("Fetching all users filters: search={}, active={}, created date from={}, date to={}, pageable={}",
                search, active, dateFrom, dateTo, pageable);

        Specification<User> spec = Specification.allOf(
                UserSpecification.search(search),
                UserSpecification.isActive(active),
                UserSpecification.dateRange(dateFrom, dateTo)
        );

        return userRepository.findAll(spec, pageable)
                .map(userMapper::entityToUserResponseDTO);
    }

    @Override
    public Optional<UserResponseDto> findUser(String id) {
        Long rawId = hashService.decode(id);
        return Optional.of(userRepository.findById(rawId)
                .map(userMapper::entityToUserResponseDTO)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id)));
    }

    @Override
    public UserResponseDto createUser(String employeeId, UserCreateDto createDTO, Authentication authentication) {
        Long rawEmployeeId = hashService.decode(employeeId);

        Employee employee = employeeRepository.findById(rawEmployeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

        if (userRepository.findByEmployeeId(employee.getId()).isPresent()) {
            throw new IllegalArgumentException("Employee already has user access. Created date at " + employee.getCreatedAt());
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found!"));

        if (userRepository.findByUsername(createDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        if (userRepository.findByEmail(createDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        User newUser = new User();
        newUser.setUsername(createDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        newUser.setFirstName(employee.getFirstName());
        newUser.setLastName(employee.getLastName());
        newUser.setEmail(createDTO.getEmail());
        newUser.setCreatedBy(currentUser);
        newUser.setUpdatedBy(currentUser);
        newUser.setEmployee(employee);

        Role employeeRole = roleRepository.findByRoleName("ROLE_EMPLOYEE")
                .orElseThrow(() -> new RuntimeException("Role EMPLOYEE not found"));
        newUser.setRoles(Collections.singleton(employeeRole));

        User createdUser = userRepository.save(newUser);
        return userMapper.entityToUserResponseDTO(createdUser);
    }

    @Override
    public UserResponseDto update(String id, UserDto userDTO, Authentication authentication) {
        Long rawId = hashService.decode(id);
        User user = userRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found!"));

        if (userDTO.getUsername() != null && !userDTO.getUsername().isBlank()) {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().isBlank()) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        user.setUpdatedBy(currentUser);
        User updatedUser = userRepository.save(user);

        return userMapper.entityToUserResponseDTO(updatedUser);
    }

    @Override
    public void archiveUser(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        User user = userRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found!"));

        user.setActive(false);
        user.setUpdatedBy(currentUser);
        userRepository.save(user);
    }

    @Override
    public void unarchivedUser(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        User user = userRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found!"));

        user.setActive(true);
        user.setUpdatedBy(currentUser);
        userRepository.save(user);
    }

    @Override
    public User getUserAuthentication(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> {
                    log.error("Authenticated user not found with username: {}", userDetails.getUsername());
                    return new ResourceNotFoundException("Authenticated user not found with username: " + userDetails.getUsername());
                });
    }
}
