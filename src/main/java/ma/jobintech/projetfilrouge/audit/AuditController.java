package ma.jobintech.projetfilrouge.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AuditLog>> getLogs(
            @RequestParam(defaultValue = "0")   int page,
            @RequestParam(defaultValue = "50")  int size,
            @RequestParam(required = false)     String action) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<AuditLog> result = action != null
            ? auditService.findByAction(action, pageable)
            : auditService.findAll(pageable);
        return ResponseEntity.ok(result);
    }
}