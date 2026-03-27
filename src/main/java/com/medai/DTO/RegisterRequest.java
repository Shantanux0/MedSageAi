package com.medai.DTO;

import com.medai.entity.Enum.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {

    @NotBlank
    private String fullName;

    @Email @NotBlank
    private String email;

    @NotBlank @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private Role role;

    // Patient fields
    private LocalDate dateOfBirth;
    private String bloodGroup;
    private String allergies;

    // Doctor fields
    private String specialization;
    private String licenseNumber;
    private String hospitalAffiliation;
}