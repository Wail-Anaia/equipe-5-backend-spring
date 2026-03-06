package ma.jobintech.projetfilrouge.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.jobintech.projetfilrouge.user.entity.Role;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String nom;
    private Role role;
    private long expiresIn; // ms
}