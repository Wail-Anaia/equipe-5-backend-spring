package ma.jobintech.projetfilrouge.user.dto;

import ma.jobintech.projetfilrouge.user.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

	@NotBlank(message = "Nom obligatoire")
    private String nom;

	@NotBlank(message = "Email obligatoire")
    @Email(message = "Email invalide")
    private String email;

    @NotBlank(message = "Mot de passe obligatoire")
    @Size(min = 6, message = "Mot de passe min 6 caractères")
    private String password;

    @NotNull(message = "Rôle obligatoire")
    private Role role;
}
