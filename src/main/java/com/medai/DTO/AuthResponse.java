package com.medai.DTO;

import com.medai.entity.Enum.Role;
import lombok.*;

@Data @Builder
public class AuthResponse {
    private String token;
    private String email;
    private String fullName;
    private Role role;
    private Long profileId;
}