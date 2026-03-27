package com.medai.service;

import com.medai.Exp.ResourceNotFoundException;
import com.medai.Repository.AuditLogRepository;
import com.medai.Repository.DoctorRepository;
import com.medai.Repository.PatientRepository;
import com.medai.Repository.UserRepository;
import com.medai.entity.AuditLog;
import com.medai.entity.Doctor;
import com.medai.entity.Patient;
import com.medai.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AuditLogRepository auditLogRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void assignDoctor(Long patientId, Long doctorId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        patient.setAssignedDoctor(doctor);
        patientRepository.save(patient);
    }

    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogRepository.findAllByOrderByTimestampDesc();
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}