package com.medai.service;

import com.medai.DTO.AuthResponse;
import com.medai.DTO.LoginRequest;
import com.medai.DTO.RegisterRequest;
import com.medai.Exp.ResourceNotFoundException;
import com.medai.Repository.DoctorRepository;
import com.medai.Repository.PatientRepository;
import com.medai.Repository.UserRepository;
import com.medai.Security.JwtUtil;
import com.medai.entity.Doctor;
import com.medai.entity.Enum.AuditAction;
import com.medai.entity.Enum.Role;
import com.medai.entity.Patient;
import com.medai.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final AuditService auditService;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = userRepository.save(User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole() != null ? req.getRole() : Role.PATIENT)
                .build());

        Long profileId = null;

        if (user.getRole() == Role.PATIENT) {
            Patient patient = patientRepository.save(Patient.builder()
                    .user(user)
                    .dateOfBirth(req.getDateOfBirth())
                    .bloodGroup(req.getBloodGroup())
                    .allergies(req.getAllergies())
                    .build());
            profileId = patient.getId();
        } else if (user.getRole() == Role.DOCTOR) {
            Doctor doctor = doctorRepository.save(Doctor.builder()
                    .user(user)
                    .specialization(req.getSpecialization())
                    .licenseNumber(req.getLicenseNumber())
                    .hospitalAffiliation(req.getHospitalAffiliation())
                    .build());
            profileId = doctor.getId();
        }

        auditService.log(user, AuditAction.LOGIN, null, "User registered");

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .profileId(profileId)
                .build();
    }

    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        auditService.log(user, AuditAction.LOGIN, null, "User logged in");

        Long profileId = switch (user.getRole()) {
            case PATIENT -> patientRepository.findByUserId(user.getId())
                    .map(Patient::getId).orElse(null);
            case DOCTOR -> doctorRepository.findByUserId(user.getId())
                    .map(Doctor::getId).orElse(null);
            default -> null;
        };

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .profileId(profileId)
                .build();
    }
}