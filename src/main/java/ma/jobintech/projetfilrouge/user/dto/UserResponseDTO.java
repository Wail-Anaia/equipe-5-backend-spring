package ma.jobintech.projetfilrouge.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.jobintech.projetfilrouge.user.entity.Role;

@Getter @AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String nom;
    private String email;
    private Role role;
    private boolean actif;
}