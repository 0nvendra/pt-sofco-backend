package com.example.test_pt_sofco_graha_gaji.id.dto;

import com.example.test_pt_sofco_graha_gaji.id.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    private String username;

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private String fullName;

    private UserRole role;
}
