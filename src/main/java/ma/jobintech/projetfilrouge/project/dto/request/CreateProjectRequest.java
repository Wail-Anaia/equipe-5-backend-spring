package ma.jobintech.projetfilrouge.project.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import ma.jobintech.projetfilrouge.common.enums.ProjectStatus;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateProjectRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 200, message = "Le titre doit contenir entre 3 et 200 caractères")
    private String titre;

    @Size(max = 2000, message = "La description ne peut pas dépasser 2000 caractères")
    private String description;

    @Size(max = 500, message = "Les technologies ne peuvent pas dépasser 500 caractères")
    private String technologies;

    private Long encadrantId;

    private Long teamId;

    private ProjectStatus status;
}