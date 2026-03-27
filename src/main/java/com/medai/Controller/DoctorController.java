package com.medai.Controller;

import com.medai.DTO.ApiResponse;
import com.medai.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/patients")
    public ResponseEntity<ApiResponse<?>> getMyPatients(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(ApiResponse.ok("Patients fetched",
                doctorService.getMyPatients(user.getUsername())));
    }

    @GetMapping("/patient/{id}/records")
    public ResponseEntity<ApiResponse<?>> getPatientRecords(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Records fetched",
                doctorService.getPatientRecords(user.getUsername(), id)));
    }

    @GetMapping("/patient/{id}/summary")
    public ResponseEntity<ApiResponse<String>> getPatientSummary(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Summary generated",
                doctorService.generatePatientSummary(user.getUsername(), id)));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<?>> getProfile(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(ApiResponse.ok("Profile fetched",
                doctorService.getDoctorByEmail(user.getUsername())));
    }
}