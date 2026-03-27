package com.medai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqUrl;

    @Value("${groq.model}")
    private String groqModel;

    // ── Extract structured data from report text ─────────────────────────
    public String extractMedicalData(String reportText) {
        String prompt = """
            You are a medical data extraction AI. From the report text below, extract:
            1. Patient vitals: hemoglobin, glucose, blood pressure, cholesterol (with units)
            2. Diagnosis or conditions noted
            3. Medications prescribed with dosage
            4. Doctor name and date of the report
            5. Flag any value outside the normal range as ANOMALY
            
            Return ONLY valid JSON in this exact format, no explanation:
            {
              "summary": "A 4-5 line overview of the patient's health status based on this report. Use simple, easy-to-understand language and avoid complex medical jargon.",
              "vitals": [{"name": "Glucose", "value": "110", "unit": "mg/dL", "normalRange": "70-100", "status": "ANOMALY"}],
              "diagnosis": ["Type 2 Diabetes - early stage"],
              "medications": [{"name": "Metformin", "dosage": "500mg", "frequency": "twice daily"}],
              "doctorName": "Dr. Smith",
              "reportDate": "2025-01-15",
              "anomalies": ["Glucose: 110 mg/dL - above normal range"]
            }
            
            Report text:
            """ + reportText;

        return callGroq(prompt);
    }

    // ── Generate full history summary for doctor ─────────────────────────
    public String generatePatientSummary(String recordsJson) {
        String prompt = """
            You are a clinical AI assistant helping a doctor prepare for a patient appointment.
            Based on the last 6 months of records provided below, give:
            1. A 3-sentence overall health summary
            2. Key trends: which vitals are improving, stable, or worsening
            3. Any anomalies that should be discussed
            4. Suggested follow-up tests or actions
            
            Return ONLY valid JSON:
            {
              "overallSummary": "...",
              "trends": [{"vital": "Glucose", "direction": "IMPROVING", "detail": "..."}],
              "anomalies": ["..."],
              "suggestions": ["..."]
            }
            
            Patient records JSON:
            """ + recordsJson;

        return callGroq(prompt);
    }

    // ── AI Chat — contextual response using patient records ──────────────
    public String chat(String userMessage, String patientRecordsContext) {
        String prompt = """
            You are MediSage AI, a personal health assistant. Answer the user's question
            based ONLY on their medical records provided below.
            Be friendly, clear, and avoid technical jargon.
            If the answer is not in the records, say so honestly.
            
            Patient's records context:
            %s
            
            User question: %s
            """.formatted(patientRecordsContext, userMessage);

        return callGroq(prompt);
    }

    // ── Core Groq API call ────────────────────────────────────────────────
    private String callGroq(String prompt) {
        Map<String, Object> body = Map.of(
                "model", groqModel,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.2
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    groqUrl, HttpMethod.POST,
                    new HttpEntity<>(body, headers), Map.class);

            var choices = (List<?>) response.getBody().get("choices");
            var message = (Map<?, ?>) ((Map<?, ?>) choices.get(0)).get("message");
            String content = message.get("content").toString().trim();
            if (content.startsWith("```json")) content = content.substring(7);
            else if (content.startsWith("```")) content = content.substring(3);
            if (content.endsWith("```")) content = content.substring(0, content.length() - 3);
            
            return content.trim();

        } catch (Exception e) {
            log.error("Groq API call failed: {}", e.getMessage());
            return "{\"error\": \"AI service temporarily unavailable\"}";
        }
    }
}