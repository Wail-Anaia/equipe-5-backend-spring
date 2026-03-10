package ma.jobintech.projetfilrouge.stats;

import lombok.RequiredArgsConstructor;
import ma.jobintech.projetfilrouge.common.enums.ProjectStatus;
import ma.jobintech.projetfilrouge.common.enums.Role;
import ma.jobintech.projetfilrouge.project.entity.Document;
import ma.jobintech.projetfilrouge.project.entity.Project;
import ma.jobintech.projetfilrouge.project.repository.DocumentRepository;
import ma.jobintech.projetfilrouge.project.repository.ProjectRepository;
import ma.jobintech.projetfilrouge.user.entity.User;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import ma.jobintech.projetfilrouge.audit.AuditLogRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import static java.util.Map.entry;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final UserRepository     userRepository;
    private final ProjectRepository  projectRepository;
    private final DocumentRepository documentRepository;
    private final AuditLogRepository auditLogRepository;
    
    public Map<String, Object> getAdminStats() {

    	List<Map<String, String>> activities =
    		    auditLogRepository
    		        .findAllByOrderByCreatedAtDesc(PageRequest.of(0,7))
    		        .getContent()
    		        .stream()
    		        .map(log -> Map.of(
    		            "action", log.getAction(),
    		            "details", log.getDetails() != null ? log.getDetails() : "",
    		            "user", log.getPerformedBy() != null
    		                    ? userRepository.findById(log.getPerformedBy())
    		                        .map(User::getNom)
    		                        .orElse("System")
    		                    : "System",
    		            "timestamp", log.getCreatedAt().toString()
    		        ))
    		        .toList();

    	return Map.ofEntries(
    		    entry("totalUsers", userRepository.count()),
    		    entry("totalProjects", projectRepository.count()),
    		    entry("totalStudents", userRepository.countByRole(Role.ETUDIANT)),
    		    entry("totalEncadrants", userRepository.countByRole(Role.ENCADRANT)),
    		    entry("pendingProjects", projectRepository.countByStatus(ProjectStatus.EN_ATTENTE)),
    		    entry("activeProjects", projectRepository.countByStatus(ProjectStatus.EN_COURS)),
    		    entry("closedProjects", projectRepository.countByStatus(ProjectStatus.TERMINE)),
    		    entry("valideProjects", projectRepository.countByStatus(ProjectStatus.VALIDE)),
    		    entry("rejeteProjects", projectRepository.countByStatus(ProjectStatus.REJETE)),
    		    entry("activeUsers", userRepository.countByActifTrue()),
    		    entry("recentActivities", activities)
    		);
    }

    public Map<String, Object> getStudentStats(Long studentId) {
        List<Project> projects = projectRepository.findByStudentId(studentId);
        List<Document> docs    = documentRepository.findByStudentIdOrderByUploadedAtDesc(studentId);

        return Map.of(
            "myProjects", projects.stream().map(p -> Map.of(
                "id",        p.getId(),
                "titre",     p.getTitre(),
                "statut",    p.getStatus().name(),
                "updatedAt", p.getUpdatedAt().toString()
            )).toList(),
            "documents", docs.stream().map(d -> Map.of(
                "id",         d.getId(),
                "nom",        d.getFileName(),
                "type",       d.getDocType().name(),
                "uploadedAt", d.getUploadedAt().toString()
            )).toList()
        );
    }

    public Map<String, Object> getEncadrantStats(Long encadrantId) {
        List<Project> supervised = projectRepository.findByEncadrantId(encadrantId);

        return Map.of(
            "supervisedProjects", supervised.stream().map(p -> Map.of(
                "id",        p.getId(),
                "titre",     p.getTitre(),
                "statut",    p.getStatus().name(),
                "updatedAt", p.getUpdatedAt().toString()
            )).toList(),
            "pendingReviews", supervised.stream()
                .filter(p -> p.getStatus() == ProjectStatus.EN_ATTENTE).count()
        );
    }
}