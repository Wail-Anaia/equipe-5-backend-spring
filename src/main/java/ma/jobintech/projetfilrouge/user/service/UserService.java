package ma.jobintech.projetfilrouge.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.jobintech.projetfilrouge.audit.AuditService;
import ma.jobintech.projetfilrouge.exception.BusinessException;
import ma.jobintech.projetfilrouge.user.dto.CreateUserRequest;
import ma.jobintech.projetfilrouge.user.dto.UserResponseDTO;
import ma.jobintech.projetfilrouge.user.entity.User;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuditService audit; // CA-6 : traçabilité
    
    public UserResponseDTO createUser(CreateUserRequest dto) {

        // CA-2 : vérification unicité email
        if (repo.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email déjà utilisé");
        }

        User user = new User();
        user.setNom(dto.getNom());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword())); // BCrypt
        user.setRole(dto.getRole());
        user.setActif(true); // CA-3 : statut actif par défaut

        User saved = repo.save(user);

        audit.log("USER_CREATED", saved.getId()); // CA-6

        return new UserResponseDTO(
            saved.getId(), saved.getNom(),
            saved.getEmail(), saved.getRole(), saved.isActif()
        );
    }
}