package com.medai.Controller;

import com.medai.DTO.ApiResponse;
import com.medai.DTO.ChatRequest;
import com.medai.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/records")
    public ResponseEntity<ApiResponse<?>> getRecords(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(ApiResponse.ok("Records fetched",
                patientService.getMyRecords(user.getUsername())));
    }

    @PostMapping("/upload-record")
    public ResponseEntity<ApiResponse<?>> uploadRecord(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam("file") MultipartFile file,
            @RequestParam("reportType") String reportType,
            @RequestParam(value = "reportName", required = false) String reportName,
            @RequestParam(value = "visitDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate)
            throws Exception {

        return ResponseEntity.ok(ApiResponse.ok("Record uploaded and analysed",
                patientService.uploadRecord(
                        user.getUsername(), file, reportType, reportName, visitDate)));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<String>> getSummary(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(ApiResponse.ok("Summary generated",
                patientService.getFullSummary(user.getUsername())));
    }

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<?>> chat(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody ChatRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("AI response",
                patientService.chat(user.getUsername(), req.getMessage())));
    }

    @GetMapping("/chat/history")
    public ResponseEntity<ApiResponse<?>> getChatHistory(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(ApiResponse.ok("Chat history",
                patientService.getChatHistory(user.getUsername())));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<?>> getProfile(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(ApiResponse.ok("Profile fetched",
                patientService.getPatientByEmail(user.getUsername())));
    }
}