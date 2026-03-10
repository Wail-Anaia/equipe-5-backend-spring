package ma.jobintech.projetfilrouge.project.service;

import ma.jobintech.projetfilrouge.project.dto.request.UploadDocumentRequest;
import ma.jobintech.projetfilrouge.project.dto.response.DocumentResponseDTO;

import java.util.List;

public interface DocumentService {
    DocumentResponseDTO uploadDocument(UploadDocumentRequest request, Long studentId);
    List<DocumentResponseDTO> getMyDocuments(Long studentId);
    List<DocumentResponseDTO> getDocumentsByProject(Long projectId);
    void deleteDocument(Long id, Long requesterId);
}