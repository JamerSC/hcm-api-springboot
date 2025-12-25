package com.jamersc.springboot.hcm_api.service.user;

import com.jamersc.springboot.hcm_api.dto.user.UserCreateDto;
import com.jamersc.springboot.hcm_api.dto.user.UserDto;
import com.jamersc.springboot.hcm_api.dto.user.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Optional;

public interface UserService {

    Page<UserResponseDto> getAllUsers(
            String search,
            Boolean active,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    );
    Optional<UserResponseDto> findUser(Long id);
    UserResponseDto createUser(Long employeeId, UserCreateDto createDTO, Authentication authentication);
    UserResponseDto update(UserDto userDTO, Authentication authentication);
    // todo
    void archiveUser(Long id, Authentication authentication);
    // todo
    void unarchivedUser(Long id, Authentication authentication);
}
