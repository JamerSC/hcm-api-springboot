package com.jamersc.springboot.hcm_api.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String id;              // Obfuscated User Hash ID
    private String employeeId;      // Obfuscated Linked Employee Hash ID
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean active;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime createdAt;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime updatedAt;

    private String updatedBy;
}