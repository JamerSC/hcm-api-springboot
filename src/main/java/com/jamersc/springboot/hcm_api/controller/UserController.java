package com.jamersc.springboot.hcm_api.controller;

import com.jamersc.springboot.hcm_api.dto.user.UserCreateDto;
import com.jamersc.springboot.hcm_api.dto.user.UserDto;
import com.jamersc.springboot.hcm_api.dto.user.UserResponseDto;
import com.jamersc.springboot.hcm_api.service.user.UserService;
import com.jamersc.springboot.hcm_api.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getAllUsers(Pageable pageable) {
        Page<UserResponseDto> retrievedUsers = userService.getAllUsers(pageable);
        ApiResponse<Page<UserResponseDto>> response = ApiResponse.<Page<UserResponseDto>>builder()
                .success(true)
                .message("List of users retrieved successfully!")
                .data(retrievedUsers)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<UserResponseDto>>> getUser(@PathVariable Long id) {
        Optional<UserResponseDto> retrievedUser = userService.findUser(id);
        ApiResponse<Optional<UserResponseDto>> response = ApiResponse.<Optional<UserResponseDto>>builder()
                .success(true)
                .message("User retrieved successfully!")
                .data(retrievedUser)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CREATE_USERS')")
    @PostMapping("/{employeeId}/create-user")
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(
            @PathVariable Long employeeId, @Valid @RequestBody UserCreateDto dto,
            Authentication authentication) {
        UserResponseDto createdUser = userService.createUser(employeeId, dto, authentication);
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User created successfully!")
                .data(createdUser)
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('UPDATE_USERS')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable Long id, @Valid @RequestBody UserDto dto,
            Authentication authentication) {
        UserResponseDto updatedUser = userService.update(dto, authentication);
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User updated successfully!")
                .data(updatedUser)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

//    @PatchMapping("/{id}/")
//    public ResponseEntity<UserResponseDto> patchUser(
//            @PathVariable Long id, @Valid @RequestBody UserCreateDto createDTO) {
//        UserResponseDto userResponseDTO = null;
//        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
//    }

    // todo improve validation
    @PreAuthorize("hasAuthority('ARCHIVE_USERS')")
    @DeleteMapping("/{id}")
    public String archiveUser(@PathVariable Long id, Authentication authentication) {
        userService.archiveUser(id, authentication);
        return "User archived successfully!";
    }

    // todo improve validation
    @PreAuthorize("hasAuthority('ARCHIVE_USERS')")
    @PatchMapping("/{id}/unarchived")
    public String unarchivedUser(@PathVariable Long id, Authentication authentication) {
        userService.unarchivedUser(id, authentication);
        return "User unarchived successfully!";
    }
}
