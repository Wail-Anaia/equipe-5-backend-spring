package ma.jobintech.projetfilrouge.audit;

import org.springframework.stereotype.Service;

@Service
public class AuditService {

    public void log(String action, Long entityId) {
        System.out.println("AUDIT: " + action + " -> " + entityId);
    }
}