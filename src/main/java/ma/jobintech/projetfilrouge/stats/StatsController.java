package ma.jobintech.projetfilrouge.stats;

import lombok.RequiredArgsConstructor;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService    statsService;
    private final UserRepository  userRepository;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> adminStats() {
        return ResponseEntity.ok(statsService.getAdminStats());
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<Map<String, Object>> studentStats(
            @AuthenticationPrincipal UserDetails principal) {
        Long studentId = userRepository.findByEmail(principal.getUsername())
                .orElseThrow().getId();
        return ResponseEntity.ok(statsService.getStudentStats(studentId));
    }

    @GetMapping("/encadrant")
    @PreAuthorize("hasRole('ENCADRANT')")
    public ResponseEntity<Map<String, Object>> encadrantStats(
            @AuthenticationPrincipal UserDetails principal) {
        Long encadrantId = userRepository.findByEmail(principal.getUsername())
                .orElseThrow().getId();
        return ResponseEntity.ok(statsService.getEncadrantStats(encadrantId));
    }
}