package com.jamersc.springboot.hcm_api.service.department;

import com.jamersc.springboot.hcm_api.dto.department.DepartmentCreateDto;
import com.jamersc.springboot.hcm_api.dto.department.DepartmentPatchDto;
import com.jamersc.springboot.hcm_api.dto.department.DepartmentResponseDto;
import com.jamersc.springboot.hcm_api.entity.Department;
import com.jamersc.springboot.hcm_api.entity.User;
import com.jamersc.springboot.hcm_api.mapper.DepartmentMapper;
import com.jamersc.springboot.hcm_api.repository.DepartmentRepository;
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
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final UserRepository userRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.userRepository = userRepository;
    }


    @Override
    public Page<DepartmentResponseDto> getAllDepartments(Pageable pageable) {
        Page<Department> departments = departmentRepository.findAll(pageable);
        return departments.map(departmentMapper::entityToDepartmentResponseDto);
    }

    @Override
    public Optional<DepartmentResponseDto> getDepartment(Long id) {
        return Optional.of(departmentRepository.findById(id)
                        .map(departmentMapper::entityToDepartmentResponseDto))
                .orElseThrow(() -> new RuntimeException("Department id found! " + id));
    }

    // Get user authentication
    private User getUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in the database"));
    }

    @Override
    public DepartmentResponseDto createDepartment(DepartmentCreateDto dto, Authentication authentication) {
        // get the current user from authentication object
        User currentUser = getUser(authentication);

        // map the dto to entity
        Department department = departmentMapper.createDtoToEntity(dto);

        // Set created/updated by with the current
        department.setCreatedBy(currentUser);
        department.setUpdatedBy(currentUser);

        // save
        Department saveDepartment = departmentRepository.save(department);


        return departmentMapper.entityToDepartmentResponseDto(saveDepartment);
    }


    @Override
    public DepartmentResponseDto updateDepartment(Long id, DepartmentPatchDto dto, Authentication authentication) {
        User currentUser = getUser(authentication);
        Department department = departmentRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Department not found"));

        // Update only provided values (PATCH = partial update)
        if (dto.getCode() != null && !dto.getCode().isBlank()) {
            department.setCode(dto.getCode());
        }

        if (dto.getName() != null && !dto.getName().isBlank()) {
            department.setName(dto.getName());
        }

        department.setUpdatedBy(currentUser);
        Department patchedDepartment = departmentRepository.save(department);
        return departmentMapper.entityToDepartmentResponseDto(patchedDepartment);
    }

    // todo improve validation
    @Override
    public void archiveDepartment(Long id, Authentication authentication) {
        //departmentRepository.deleteById(id);
    }

    // todo improve validation
    @Override
    public DepartmentResponseDto unarchivedDepartment(Long id, Authentication authentication) {
        return null;
    }
}
