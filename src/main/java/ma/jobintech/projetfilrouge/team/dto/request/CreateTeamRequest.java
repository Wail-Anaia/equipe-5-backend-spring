package ma.jobintech.projetfilrouge.team.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateTeamRequest {

    @NotBlank(message = "Le nom de l'équipe est obligatoire")
    @Size(min = 2, max = 100)
    private String nom;

    private Set<Long> memberIds;
}