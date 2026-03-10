package ma.jobintech.projetfilrouge.user.dto.response;

import lombok.*;
import ma.jobintech.projetfilrouge.common.enums.Role;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserResponseDTO {
    private Long          id;
    private String        nom;
    private String        email;
    private Role          role;
    private Boolean       actif;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}