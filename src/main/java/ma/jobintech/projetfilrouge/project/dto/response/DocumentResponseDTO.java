package ma.jobintech.projetfilrouge.project.dto.response;

import lombok.*;
import ma.jobintech.projetfilrouge.common.enums.DocumentType;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DocumentResponseDTO {
    private Long          id;
    private String        fileName;
    private String        filePath;
    private Long          fileSize;
    private DocumentType  docType;
    private Long          projectId;
    private String        projectTitre;
    private Long          studentId;
    private String        studentNom;
    private LocalDateTime uploadedAt;
}