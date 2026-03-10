package ma.jobintech.projetfilrouge.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateUserStatusRequest {

    @NotNull(message = "Le statut actif est obligatoire")
    private Boolean actif;
}