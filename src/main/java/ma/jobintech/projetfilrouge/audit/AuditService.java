package ma.jobintech.projetfilrouge.audit;

import ma.jobintech.projetfilrouge.user.entity.AuditLog;
import ma.jobintech.projetfilrouge.user.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public static final String USER_CREATED    = "USER_CREATED";
    public static final String USER_DISABLED   = "USER_DISABLED";
    public static final String USER_ENABLED    = "USER_ENABLED";
    public static final String LOGIN_SUCCESS   = "LOGIN_SUCCESS";
    public static final String LOGIN_FAILED    = "LOGIN_FAILED";
    public static final String ROLE_CHANGED    = "ROLE_CHANGED";

    @Async
    public void log(String action, Long targetUserId, String details) {
        Long performedBy = getCurrentUserId();

        AuditLog auditLog = AuditLog.builder()
                .action(action)
                .userId(targetUserId)
                .performedBy(performedBy)
                .details(details)
                .build();

        auditLogRepository.save(auditLog);
        log.info("[AUDIT] action={} targetUser={} performedBy={} details={}",
                action, targetUserId, performedBy, details);
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        // En pratique, on résoudrait l'email → ID via UserRepository
        // Ici on retourne null si pas de contexte (ex: lors du login)
        return null;
    }
}