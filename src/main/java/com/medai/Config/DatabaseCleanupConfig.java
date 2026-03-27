package com.medai.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DatabaseCleanupConfig {

    @Bean
    @org.springframework.transaction.annotation.Transactional
    public CommandLineRunner cleanupDatabase(jakarta.persistence.EntityManager entityManager) {
        return args -> {
            log.info("Starting manual database cleanup...");
            try {
                // Using native query to truncate all tables with CASCADE to handle foreign keys
                // We'll reset identity too so the user IDs start from 1 again.
                entityManager.createNativeQuery("TRUNCATE TABLE audit_logs, chat_history, medical_records, ai_summaries, patients, doctors, users RESTART IDENTITY CASCADE").executeUpdate();
                log.info("Successfully cleaned and reset the entire database.");
            } catch (Exception e) {
                log.error("Failed to clean database: {}", e.getMessage());
            }
        };
    }
}
