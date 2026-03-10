package ma.jobintech.projetfilrouge.project.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import ma.jobintech.projetfilrouge.common.enums.DocumentType;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UploadDocumentRequest {

    @NotBlank(message = "Le nom du fichier est obligatoire")
    private String fileName;

    @NotBlank(message = "Le chemin du fichier est obligatoire")
    private String filePath;

    private Long fileSize;

    private DocumentType docType;

    @NotNull(message = "L'identifiant du projet est obligatoire")
    private Long projectId;
}