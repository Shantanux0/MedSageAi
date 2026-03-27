package com.medai.entity;


import com.medai.entity.Enum.RecordStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Doctor doctor;

    private String reportType;      // BLOOD, SCAN, PRESCRIPTION, XRAY
    private String reportName;
    private LocalDate visitDate;
    @Column(length = 1000)
    private String fileUrl;         // S3 URL or local path
    
    @Column(length = 1000)
    private String originalFileName;

    // Stores AI-extracted JSON: vitals, diagnosis, medications etc.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String extractedData;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RecordStatus status = RecordStatus.PENDING;

    @OneToOne(mappedBy = "record", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private AiSummary aiSummary;

    @CreationTimestamp
    private LocalDateTime createdAt;
}