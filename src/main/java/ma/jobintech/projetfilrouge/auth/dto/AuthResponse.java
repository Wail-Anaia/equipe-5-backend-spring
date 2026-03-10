package ma.jobintech.projetfilrouge.auth.dto;

import lombok.*;
import ma.jobintech.projetfilrouge.common.enums.Role;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponse {
    private String token;
    private String email;
    private String nom;
    private Role   role;
}