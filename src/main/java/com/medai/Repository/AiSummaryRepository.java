package com.medai.Repository;


import com.medai.entity.AiSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AiSummaryRepository extends JpaRepository<AiSummary, Long> {
    Optional<AiSummary> findByRecordId(Long recordId);
}