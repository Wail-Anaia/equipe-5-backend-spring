package ma.jobintech.projetfilrouge.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.jobintech.projetfilrouge.common.enums.Role;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String nom;
    private String email;
    private Role role;
    private boolean actif;
    private LocalDateTime createdAt;
}