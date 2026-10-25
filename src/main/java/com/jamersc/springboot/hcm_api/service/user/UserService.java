package com.jamersc.springboot.hcm_api.service.user;

import com.jamersc.springboot.hcm_api.dto.user.UserCreateDto;
import com.jamersc.springboot.hcm_api.dto.user.UserDto;
import com.jamersc.springboot.hcm_api.dto.user.UserResponseDto;
import com.jamersc.springboot.hcm_api.entity.User;
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

    Optional<UserResponseDto> findUser(String id);

    UserResponseDto createUser(String employeeId, UserCreateDto createDTO, Authentication authentication);

    UserResponseDto update(String id, UserDto userDTO, Authentication authentication);

    void archiveUser(String id, Authentication authentication);

    void unarchivedUser(String id, Authentication authentication);

    User getUserAuthentication(Authentication authentication);
}
