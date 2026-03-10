package ma.jobintech.projetfilrouge.team.dto.response;

import lombok.*;
import ma.jobintech.projetfilrouge.user.dto.response.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TeamResponseDTO {
    private Long                    id;
    private String                  nom;
    private Set<UserResponseDTO>    members;
    private LocalDateTime           createdAt;
}