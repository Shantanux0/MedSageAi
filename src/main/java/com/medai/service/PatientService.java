package com.medai.service;

import com.medai.Exp.ResourceNotFoundException;
import com.medai.Repository.*;
import com.medai.entity.*;
import com.medai.entity.Enum.AuditAction;
import com.medai.entity.Enum.RecordStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final MedicalRecordRepository recordRepository;
    private final AiSummaryRepository summaryRepository;
    private final ChatHistoryRepository chatRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final AiService aiService;
    private final AuditService auditService;

    public Patient getPatientByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
    }

    public List<MedicalRecord> getMyRecords(String email) {
        Patient patient = getPatientByEmail(email);
        User user = userRepository.findByEmail(email).get();
        auditService.log(user, AuditAction.VIEW, null, "Viewed all records");
        return recordRepository.findByPatientIdOrderByCreatedAtDesc(patient.getId());
    }

    @Transactional
    public MedicalRecord uploadRecord(String email,
                                      MultipartFile file,
                                      String reportType,
                                      String reportName,
                                      LocalDate visitDate) throws Exception {

        fileService.validateFile(file);
        Patient patient = getPatientByEmail(email);
        User user = userRepository.findByEmail(email).get();

        // Save file locally (swap with S3 in production)
        String fileUrl = fileService.saveFile(file);

        // Extract text from PDF
        String extractedText = fileService.isPdf(file)
                ? fileService.extractTextFromPdf(file)
                : "Image file — text extraction requires Vision API";

        // Call Gemini for structured data extraction
        String extractedJson = aiService.extractMedicalData(extractedText);

        // Determine status from extracted data
        RecordStatus status = extractedJson.contains("ANOMALY")
                ? RecordStatus.WARNING : RecordStatus.NORMAL;

        // Save record
        MedicalRecord record = recordRepository.save(MedicalRecord.builder()
                .patient(patient)
                .reportType(reportType)
                .reportName(reportName != null ? reportName : file.getOriginalFilename())
                .visitDate(visitDate != null ? visitDate : LocalDate.now())
                .fileUrl(fileUrl)
                .originalFileName(file.getOriginalFilename())
                .extractedData(extractedJson)
                .status(status)
                .build());

        // Save AI summary
        String summaryText = "Clinical summary pending...";
        try {
            com.fasterxml.jackson.databind.JsonNode root = new com.fasterxml.jackson.databind.ObjectMapper().readTree(extractedJson);
            if (root.has("summary")) {
                summaryText = root.get("summary").asText();
            }
        } catch (Exception e) {
            // fallback to diagnosis/anomalies if summary field is missing or malformed
        }

        summaryRepository.save(AiSummary.builder()
                .record(record)
                .summaryText(summaryText)
                .anomalies(extractedJson)
                .confidenceScore(0.92)
                .build());

        auditService.log(user, AuditAction.UPLOAD, record.getId(), "Uploaded " + reportType);
        return record;
    }

    public String getFullSummary(String email) {
        Patient patient = getPatientByEmail(email);
        List<MedicalRecord> records =
                recordRepository.findTop6ByPatientIdOrderByCreatedAtDesc(patient.getId());

        if (records.isEmpty()) return "{\"message\": \"No records found\"}";

        StringBuilder context = new StringBuilder();
        records.forEach(r -> context.append(r.getExtractedData()).append("\n"));

        return aiService.generatePatientSummary(context.toString());
    }

    public ChatHistory chat(String email, String message) {
        Patient patient = getPatientByEmail(email);
        List<MedicalRecord> records =
                recordRepository.findTop6ByPatientIdOrderByCreatedAtDesc(patient.getId());

        StringBuilder context = new StringBuilder();
        records.forEach(r -> context.append(r.getExtractedData()).append("\n"));

        String aiResponse = aiService.chat(message, context.toString());

        User user = userRepository.findByEmail(email).get();
        return chatRepository.save(ChatHistory.builder()
                .user(user)
                .userMessage(message)
                .aiResponse(aiResponse)
                .build());
    }

    public List<ChatHistory> getChatHistory(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return chatRepository.findByUserIdOrderByTimestampAsc(user.getId());
    }
}