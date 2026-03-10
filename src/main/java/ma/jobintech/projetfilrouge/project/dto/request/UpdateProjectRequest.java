package ma.jobintech.projetfilrouge.project.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import ma.jobintech.projetfilrouge.common.enums.ProjectStatus;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateProjectRequest {

    @Size(min = 3, max = 200)
    private String titre;

    @Size(max = 2000)
    private String description;

    @Size(max = 500)
    private String technologies;

    private ProjectStatus status;

    private Long encadrantId;

    private Long teamId;
}