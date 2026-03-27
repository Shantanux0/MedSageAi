package com.medai.service;

import com.medai.Exp.ResourceNotFoundException;
import com.medai.Repository.DoctorRepository;
import com.medai.Repository.MedicalRecordRepository;
import com.medai.Repository.PatientRepository;
import com.medai.Repository.UserRepository;
import com.medai.entity.Doctor;
import com.medai.entity.Enum.AuditAction;
import com.medai.entity.MedicalRecord;
import com.medai.entity.Patient;
import com.medai.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final MedicalRecordRepository recordRepository;
    private final UserRepository userRepository;
    private final AiService aiService;
    private final AuditService auditService;

    public Doctor getDoctorByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));
    }

    public List<Patient> getMyPatients(String email) {
        Doctor doctor = getDoctorByEmail(email);
        return patientRepository.findAll().stream()
                .filter(p -> p.getAssignedDoctor() != null
                        && p.getAssignedDoctor().getId().equals(doctor.getId()))
                .toList();
    }

    public List<MedicalRecord> getPatientRecords(String doctorEmail, Long patientId) {
        Doctor doctor = getDoctorByEmail(doctorEmail);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        User doctorUser = userRepository.findByEmail(doctorEmail).get();
        auditService.log(doctorUser, AuditAction.VIEW, patientId,
                "Doctor viewed patient records");

        return recordRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    public String generatePatientSummary(String doctorEmail, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        List<MedicalRecord> records =
                recordRepository.findTop6ByPatientIdOrderByCreatedAtDesc(patientId);

        if (records.isEmpty()) return "{\"message\": \"No records available\"}";

        StringBuilder context = new StringBuilder();
        records.forEach(r -> context.append(r.getExtractedData()).append("\n"));

        User doctorUser = userRepository.findByEmail(doctorEmail).get();
        auditService.log(doctorUser, AuditAction.GENERATE_SUMMARY, patientId,
                "Generated AI summary for patient " + patientId);

        return aiService.generatePatientSummary(context.toString());
    }
}