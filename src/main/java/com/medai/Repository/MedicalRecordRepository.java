// MedicalRecordRepository.java
package com.medai.Repository;


import com.medai.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<MedicalRecord> findTop6ByPatientIdOrderByCreatedAtDesc(Long patientId);
}