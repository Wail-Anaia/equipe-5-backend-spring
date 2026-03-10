package ma.jobintech.projetfilrouge.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    // ── Action constants ──────────────────────────────
    public static final String USER_CREATED   = "USER_CREATED";
    public static final String USER_DISABLED  = "USER_DISABLED";
    public static final String USER_ENABLED   = "USER_ENABLED";
    public static final String USER_UPDATED   = "USER_UPDATED";
    public static final String LOGIN_SUCCESS  = "LOGIN_SUCCESS";
    public static final String LOGIN_FAILED   = "LOGIN_FAILED";
    public static final String ROLE_CHANGED   = "ROLE_CHANGED";
    public static final String PROJECT_CREATED = "PROJECT_CREATED";
    public static final String DOCUMENT_UPLOADED = "DOCUMENT_UPLOADED";

    private final AuditLogRepository auditLogRepository;

    @Async
    public void log(String action, Long userId, Long performedBy, String details) {
        try {
            AuditLog entry = AuditLog.builder()
                    .action(action)
                    .userId(userId)
                    .performedBy(performedBy)
                    .details(details)
                    .build();
            auditLogRepository.save(entry);
        } catch (Exception e) {
            log.error("Audit logging failed for action {}: {}", action, e.getMessage());
        }
    }

    public Page<AuditLog> findAll(Pageable pageable) {
        return auditLogRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<AuditLog> findByAction(String action, Pageable pageable) {
        return auditLogRepository.findByActionOrderByCreatedAtDesc(action, pageable);
    }
}