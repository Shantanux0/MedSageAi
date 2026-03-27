package com.medai.Controller;

import com.medai.DTO.ApiResponse;
import com.medai.DTO.AssignDoctorRequest;
import com.medai.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.ok("Users fetched",
                adminService.getAllUsers()));
    }

    @GetMapping("/doctors")
    public ResponseEntity<ApiResponse<?>> getAllDoctors() {
        return ResponseEntity.ok(ApiResponse.ok("Doctors fetched",
                adminService.getAllDoctors()));
    }

    @GetMapping("/patients")
    public ResponseEntity<ApiResponse<?>> getAllPatients() {
        return ResponseEntity.ok(ApiResponse.ok("Patients fetched",
                adminService.getAllPatients()));
    }

    @PutMapping("/assign-doctor")
    public ResponseEntity<ApiResponse<?>> assignDoctor(
            @RequestBody AssignDoctorRequest req) {
        adminService.assignDoctor(req.getPatientId(), req.getDoctorId());
        return ResponseEntity.ok(ApiResponse.ok("Doctor assigned successfully", null));
    }

    @PutMapping("/deactivate/{userId}")
    public ResponseEntity<ApiResponse<?>> deactivate(@PathVariable Long userId) {
        adminService.deactivateUser(userId);
        return ResponseEntity.ok(ApiResponse.ok("User deactivated", null));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<?>> getAuditLogs() {
        return ResponseEntity.ok(ApiResponse.ok("Audit logs fetched",
                adminService.getAuditLogs()));
    }
}