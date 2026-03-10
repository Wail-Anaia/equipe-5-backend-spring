package ma.jobintech.projetfilrouge.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.jobintech.projetfilrouge.audit.AuditService;
import ma.jobintech.projetfilrouge.common.enums.DocumentType;
import ma.jobintech.projetfilrouge.exception.types.BusinessException;
import ma.jobintech.projetfilrouge.project.dto.request.UploadDocumentRequest;
import ma.jobintech.projetfilrouge.project.dto.response.DocumentResponseDTO;
import ma.jobintech.projetfilrouge.project.entity.Document;
import ma.jobintech.projetfilrouge.project.entity.Project;
import ma.jobintech.projetfilrouge.project.repository.DocumentRepository;
import ma.jobintech.projetfilrouge.project.repository.ProjectRepository;
import ma.jobintech.projetfilrouge.user.entity.User;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ProjectRepository  projectRepository;
    private final UserRepository     userRepository;
    private final AuditService       auditService;

    @Override
    @Transactional
    public DocumentResponseDTO uploadDocument(UploadDocumentRequest request, Long studentId) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new BusinessException("Projet introuvable : " + request.getProjectId()));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new BusinessException("Étudiant introuvable : " + studentId));

        DocumentType docType = request.getDocType() != null
            ? request.getDocType()
            : inferDocType(request.getFileName());

        Document doc = Document.builder()
                .fileName(request.getFileName())
                .filePath(request.getFilePath())
                .fileSize(request.getFileSize())
                .docType(docType)
                .project(project)
                .student(student)
                .build();

        Document saved = documentRepository.save(doc);

        auditService.log(
            AuditService.DOCUMENT_UPLOADED, studentId, studentId,
            "Document uploadé : " + saved.getFileName() + " | Projet : " + project.getTitre()
        );

        return toDto(saved);
    }

    @Override
    public List<DocumentResponseDTO> getMyDocuments(Long studentId) {
        return documentRepository.findByStudentIdOrderByUploadedAtDesc(studentId)
                .stream().map(this::toDto).toList();
    }

    @Override
    public List<DocumentResponseDTO> getDocumentsByProject(Long projectId) {
        return documentRepository.findByProjectIdOrderByUploadedAtDesc(projectId)
                .stream().map(this::toDto).toList();
    }

    @Override
    @Transactional
    public void deleteDocument(Long id, Long requesterId) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Document introuvable : " + id));

        boolean isOwner = doc.getStudent() != null && doc.getStudent().getId().equals(requesterId);
        boolean isAdmin  = userRepository.findById(requesterId)
                .map(u -> u.getRole().name().equals("ADMIN")).orElse(false);

        if (!isOwner && !isAdmin) {
            throw new BusinessException("Vous n'avez pas le droit de supprimer ce document");
        }

        documentRepository.delete(doc);
    }

    // ── Infer document type from extension ────────────
    private DocumentType inferDocType(String fileName) {
        if (fileName == null) return DocumentType.OTHER;
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".pdf"))                          return DocumentType.PDF;
        if (lower.endsWith(".docx") || lower.endsWith(".doc")) return DocumentType.DOCX;
        if (lower.endsWith(".pptx") || lower.endsWith(".ppt")) return DocumentType.PPTX;
        if (lower.endsWith(".xlsx") || lower.endsWith(".xls")) return DocumentType.XLSX;
        if (lower.endsWith(".zip"))                          return DocumentType.ZIP;
        return DocumentType.OTHER;
    }

    private DocumentResponseDTO toDto(Document d) {
        return DocumentResponseDTO.builder()
                .id(d.getId())
                .fileName(d.getFileName())
                .filePath(d.getFilePath())
                .fileSize(d.getFileSize())
                .docType(d.getDocType())
                .projectId(d.getProject() != null  ? d.getProject().getId()   : null)
                .projectTitre(d.getProject() != null ? d.getProject().getTitre() : null)
                .studentId(d.getStudent() != null  ? d.getStudent().getId()   : null)
                .studentNom(d.getStudent() != null ? d.getStudent().getNom()  : null)
                .uploadedAt(d.getUploadedAt())
                .build();
    }
}