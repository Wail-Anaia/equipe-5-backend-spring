package ma.jobintech.projetfilrouge.user.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ma.jobintech.projetfilrouge.user.entity.Role;
import ma.jobintech.projetfilrouge.user.entity.User;
import ma.jobintech.projetfilrouge.user.dto.UserResponseDTO;

import org.springframework.security.crypto.password.PasswordEncoder;

import ma.jobintech.projetfilrouge.audit.AuditService;
import ma.jobintech.projetfilrouge.exception.BusinessException;
import ma.jobintech.projetfilrouge.user.dto.CreateUserRequest;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository repo;
    @Mock PasswordEncoder encoder;
    @Mock AuditService audit;
    @InjectMocks UserService service;

    // ── T-B01 : Création nominale ──────────────────────────────────────
    @Test
    void createUser_validInput_returnsDTO() {
    	CreateUserRequest req =
    		    new CreateUserRequest("Ali", "ali@test.com", "123456", Role.ETUDIANT);

        when(repo.existsByEmail("ali@test.com")).thenReturn(false);
        when(encoder.encode("123456")).thenReturn("hashed");
        when(repo.save(any())).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L); return u;
        });

        UserResponseDTO result = service.createUser(req);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.isActif()).isTrue(); // CA-3
        verify(audit).log("USER_CREATED", 1L); // CA-6
    }

    // ── T-B02 : Email dupliqué → BusinessException ────────────────────
    @Test
    void createUser_duplicateEmail_throwsBusinessException() {
        when(repo.existsByEmail("existing@test.com")).thenReturn(true);
        CreateUserRequest req =
        	    new CreateUserRequest("Test", "existing@test.com", "123456", Role.ETUDIANT);

        assertThatThrownBy(() -> service.createUser(req))
            .isInstanceOf(BusinessException.class)
            .hasMessage("Email déjà utilisé"); // CA-2
    }
}
