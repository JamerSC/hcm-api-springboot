package com.jamersc.springboot.hcm_api.service.user;

import com.jamersc.springboot.hcm_api.dto.user.UserCreateDto;
import com.jamersc.springboot.hcm_api.dto.user.UserDto;
import com.jamersc.springboot.hcm_api.dto.user.UserResponseDto;
import com.jamersc.springboot.hcm_api.entity.Employee;
import com.jamersc.springboot.hcm_api.entity.Role;
import com.jamersc.springboot.hcm_api.entity.User;
import com.jamersc.springboot.hcm_api.mapper.UserMapper;
import com.jamersc.springboot.hcm_api.repository.EmployeeRepository;
import com.jamersc.springboot.hcm_api.repository.RoleRepository;
import com.jamersc.springboot.hcm_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, EmployeeRepository employeeRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::entityToUserResponseDTO);
    }

    @Override
    public Optional<UserResponseDto> findUser(Long id) {
        return Optional.ofNullable(userRepository.findById(id)
                .map(userMapper::entityToUserResponseDTO)
                .orElseThrow(()-> new RuntimeException("User not found.")));
    }

    @Override
    public UserResponseDto createUser(Long employeeId, UserCreateDto createDTO, Authentication authentication) {
        // fetch employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new RuntimeException("Employee not found"));

        if (userRepository.findByEmployeeId(employee.getId()).isPresent()) {
            throw new IllegalArgumentException("Employee have already an user access. Created date at " + employee.getCreatedAt());
        }

        // check current user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found!"));

        // check if the username & email is already taken
        if (userRepository.findByUsername(createDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken.");
        }

//        if (userRepository.findByEmail(employee.getEmail()).isPresent()) {
//            throw new IllegalArgumentException("Username is already in use.");
//        }

        User newUser = new User();
        newUser.setUsername(createDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        newUser.setFirstName(employee.getFirstName());
        newUser.setLastName(employee.getLastName());
        newUser.setEmail(createDTO.getEmail());
        newUser.setCreatedBy(currentUser);
        newUser.setUpdatedBy(currentUser);

        // assign link user to employee
        newUser.setEmployee(employee);

        // assign default role
        Role employeeRole = roleRepository.findByRoleName("ROLE_EMPLOYEE")
                .orElseThrow(()-> new RuntimeException("Role Employee not found"));
        newUser.setRoles(Collections.singleton(employeeRole));

        // save user in database
        User createdUser = userRepository.save(newUser);

        return userMapper.entityToUserResponseDTO(createdUser);
    }

    @Override
    public UserResponseDto update(UserDto userDTO, Authentication authentication) {
        return null;
    }

    @Override
    public void archiveUser(Long id, Authentication authentication) {
        // todo
    }

    @Override
    public void unarchivedUser(Long id, Authentication authentication) {
        // todo
    }
}
