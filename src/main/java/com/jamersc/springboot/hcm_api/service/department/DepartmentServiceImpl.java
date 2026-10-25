package com.jamersc.springboot.hcm_api.service.department;

import com.jamersc.springboot.hcm_api.dto.department.DepartmentCreateDto;
import com.jamersc.springboot.hcm_api.dto.department.DepartmentPatchDto;
import com.jamersc.springboot.hcm_api.dto.department.DepartmentResponseDto;
import com.jamersc.springboot.hcm_api.entity.Department;
import com.jamersc.springboot.hcm_api.entity.User;
import com.jamersc.springboot.hcm_api.mapper.DepartmentMapper;
import com.jamersc.springboot.hcm_api.repository.DepartmentRepository;
import com.jamersc.springboot.hcm_api.repository.DepartmentSpecification;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final UserService userService;
    private final HashidService hashService;

    @Override
    public Page<DepartmentResponseDto> getAllDepartments(
            String search,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    ) {
        log.debug("Fetching departments with filters - Search: {}, date from {}, date to {}",
                search,
                dateFrom,
                dateTo
        );

        Specification<Department> spec = Specification.allOf(
                DepartmentSpecification.search(search),
                DepartmentSpecification.dateRange(dateFrom, dateTo)
        );

        Page<Department> departments = departmentRepository.findAll(spec, pageable);

        return departments.map(departmentMapper::entityToDepartmentResponseDto);
    }


//    @Override
//    public Page<DepartmentResponseDto> getAllDepartments(Pageable pageable) {
//        Page<Department> departments = departmentRepository.findAll(pageable);
//        return departments.map(departmentMapper::entityToDepartmentResponseDto);
//    }

    @Override
    public Optional<DepartmentResponseDto> getDepartment(String id) {
        Long rawId = hashService.decode(id);
        return Optional.of(departmentRepository.findById(rawId)
                        .map(departmentMapper::entityToDepartmentResponseDto))
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    @Override
    public DepartmentResponseDto createDepartment(DepartmentCreateDto dto, Authentication authentication) {
        // get the current user from authentication object
        User currentUser = userService.getUserAuthentication(authentication);

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
    public DepartmentResponseDto updateDepartment(String id, DepartmentPatchDto dto, Authentication authentication) {
        User currentUser = userService.getUserAuthentication(authentication);
        Long rawId = hashService.decode(id);

        Department department = departmentRepository.findById(rawId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

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

    @Override
    public void archiveDepartment(String id, Authentication authentication) {
        Long rawId = hashService.decode(id);
        departmentRepository.deleteById(rawId);
    }

    @Override
    public DepartmentResponseDto unarchivedDepartment(String id, Authentication authentication) {
        return null;
    }
}
