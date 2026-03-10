package ma.jobintech.projetfilrouge.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.jobintech.projetfilrouge.project.dto.request.CreateProjectRequest;
import ma.jobintech.projetfilrouge.project.dto.request.UpdateProjectRequest;
import ma.jobintech.projetfilrouge.project.dto.response.ProjectResponseDTO;
import ma.jobintech.projetfilrouge.project.service.ProjectService;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService  projectService;
    private final UserRepository  userRepository;

    // ── ADMIN : créer un projet ───────────────────────
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectResponseDTO> create(
            @Valid @RequestBody CreateProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(request));
    }

    // ── ADMIN / ENCADRANT : liste paginée ─────────────
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ENCADRANT')")
    public ResponseEntity<Page<ProjectResponseDTO>> getAll(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(projectService.getAllProjects(pageable));
    }

    // ── Tous rôles : détail d'un projet ───────────────
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ENCADRANT','ETUDIANT')")
    public ResponseEntity<ProjectResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    // ── ADMIN / ENCADRANT : modifier un projet ────────
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ENCADRANT')")
    public ResponseEntity<ProjectResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectRequest request) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    // ── ADMIN : supprimer ─────────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // ── ENCADRANT : ses propres projets ───────────────
    @GetMapping("/my")
    @PreAuthorize("hasRole('ENCADRANT')")
    public ResponseEntity<List<ProjectResponseDTO>> myProjects(
            @AuthenticationPrincipal UserDetails principal) {
        Long encadrantId = userRepository.findByEmail(principal.getUsername())
                .orElseThrow().getId();
        return ResponseEntity.ok(projectService.getProjectsByEncadrant(encadrantId));
    }

    // ── ETUDIANT : ses projets ────────────────────────
    @GetMapping("/student")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<List<ProjectResponseDTO>> studentProjects(
            @AuthenticationPrincipal UserDetails principal) {
        Long studentId = userRepository.findByEmail(principal.getUsername())
                .orElseThrow().getId();
        return ResponseEntity.ok(projectService.getProjectsByStudent(studentId));
    }
}