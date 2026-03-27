package com.medai.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_summaries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AiSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "record_id", nullable = false)
    private MedicalRecord record;

    @Column(columnDefinition = "TEXT")
    private String summaryText;

    // JSON array of anomaly strings
    @Column(columnDefinition = "TEXT")
    private String anomalies;

    private Double confidenceScore;

    @CreationTimestamp
    private LocalDateTime createdAt;
}