package ma.jobintech.projetfilrouge.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.jobintech.projetfilrouge.project.dto.request.UploadDocumentRequest;
import ma.jobintech.projetfilrouge.project.dto.response.DocumentResponseDTO;
import ma.jobintech.projetfilrouge.project.service.DocumentService;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final UserRepository  userRepository;

    // ── ETUDIANT : uploader un document ───────────────
    @PostMapping
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<DocumentResponseDTO> upload(
            @Valid @RequestBody UploadDocumentRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        Long studentId = resolveId(principal);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentService.uploadDocument(request, studentId));
    }

    // ── ETUDIANT : ses documents ──────────────────────
    @GetMapping("/my")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<List<DocumentResponseDTO>> myDocuments(
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(documentService.getMyDocuments(resolveId(principal)));
    }

    // ── ADMIN / ENCADRANT : documents d'un projet ─────
    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN','ENCADRANT')")
    public ResponseEntity<List<DocumentResponseDTO>> byProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(documentService.getDocumentsByProject(projectId));
    }

    // ── ETUDIANT / ADMIN : supprimer un document ──────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        documentService.deleteDocument(id, resolveId(principal));
        return ResponseEntity.noContent().build();
    }

    private Long resolveId(UserDetails principal) {
        return userRepository.findByEmail(principal.getUsername())
                .orElseThrow().getId();
    }
}