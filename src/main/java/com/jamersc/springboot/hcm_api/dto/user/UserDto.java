package com.jamersc.springboot.hcm_api.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;              // Obfuscated User Hash ID

    @NotBlank(message = "Username is required")
    @Size(min = 8, max = 30, message = "Username must be between 8 and 30 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters")
    private String password;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
}
