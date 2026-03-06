package ma.jobintech.projetfilrouge.user.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import ma.jobintech.projetfilrouge.user.entity.Role;

@Getter @Setter
public class CreateUserRequest {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au minimum 6 caractères")
    private String password;

    @NotNull(message = "Le rôle est obligatoire")
    private Role role;
}