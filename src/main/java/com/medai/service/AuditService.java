package com.medai.service;

import com.medai.Repository.AuditLogRepository;
import com.medai.entity.AuditLog;
import com.medai.entity.Enum.AuditAction;
import com.medai.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void log(User user, AuditAction action, Long recordId, String details) {
        auditLogRepository.save(AuditLog.builder()
                .user(user)
                .action(action)
                .targetRecordId(recordId)
                .details(details)
                .build());
    }
}