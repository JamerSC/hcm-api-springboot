package com.jamersc.springboot.hcm_api.controller;

import com.jamersc.springboot.hcm_api.dto.user.UserCreateDto;
import com.jamersc.springboot.hcm_api.dto.user.UserDto;
import com.jamersc.springboot.hcm_api.dto.user.UserResponseDto;
import com.jamersc.springboot.hcm_api.service.user.UserService;
import com.jamersc.springboot.hcm_api.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping("/")
    public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<UserResponseDto> retrievedUsers = userService.getAllUsers(
                search,
                active,
                dateFrom,
                dateTo,
                pageable
        );
        return ResponseEntity.ok(ApiResponse.<Page<UserResponseDto>>builder()
                .success(true)
                .message("List of users retrieved successfully!")
                .data(retrievedUsers)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<UserResponseDto>>> getUser(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<Optional<UserResponseDto>>builder()
                .success(true)
                .message("User retrieved successfully!")
                .data(userService.findUser(id))
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('CREATE_USERS')")
    @PostMapping("/{employeeId}/create-user")
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(
            @PathVariable String employeeId,
            @Valid @RequestBody UserCreateDto dto,
            Authentication authentication) {
        UserResponseDto createdUser = userService.createUser(employeeId, dto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User created successfully!")
                .data(createdUser)
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('UPDATE_USERS')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserDto dto,
            Authentication authentication) {
        UserResponseDto updatedUser = userService.update(id, dto, authentication);
        return ResponseEntity.ok(ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User updated successfully!")
                .data(updatedUser)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('ARCHIVE_USERS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> archiveUser(
            @PathVariable String id,
            Authentication authentication) {
        userService.archiveUser(id, authentication);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("User archived successfully!")
                .data(null)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasAuthority('ARCHIVE_USERS')")
    @PatchMapping("/{id}/unarchived")
    public ResponseEntity<ApiResponse<Void>> unarchivedUser(
            @PathVariable String id,
            Authentication authentication) {
        userService.unarchivedUser(id, authentication);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("User unarchived successfully!")
                .data(null)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build());
    }
}
