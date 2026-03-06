package ma.jobintech.projetfilrouge.user.service;

import ma.jobintech.projetfilrouge.audit.AuditService;
import ma.jobintech.projetfilrouge.user.dto.request.CreateUserRequest;
import ma.jobintech.projetfilrouge.user.dto.response.UserResponse;
import ma.jobintech.projetfilrouge.user.entity.Role;
import ma.jobintech.projetfilrouge.user.entity.User;
import ma.jobintech.projetfilrouge.exception.BusinessException;
import ma.jobintech.projetfilrouge.user.mapper.UserMapper;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuditService auditService;
    @Mock private UserMapper userMapper;
    @InjectMocks private UserService userService;

    @Test
    void createUser_validInput_returnsUserResponse() {
        // Arrange
        CreateUserRequest req = new CreateUserRequest();
        req.setNom("Alice Martin");
        req.setEmail("alice@university.ma");
        req.setPassword("securePass");
        req.setRole(Role.ETUDIANT);

        User savedUser = User.builder()
                .id(1L).nom("Alice Martin").email("alice@university.ma")
                .password("hashed").role(Role.ETUDIANT).actif(true)
                .createdAt(LocalDateTime.now()).build();

        UserResponse expectedResponse = new UserResponse(
                1L, "Alice Martin", "alice@university.ma",
                Role.ETUDIANT, true, LocalDateTime.now());

        when(userRepository.existsByEmail("alice@university.ma")).thenReturn(false);
        when(passwordEncoder.encode("securePass")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(expectedResponse);

        // Act
        UserResponse result = userService.createUser(req);

        // Assert
        assertThat(result.getEmail()).isEqualTo("alice@university.ma");
        assertThat(result.isActif()).isTrue();
        verify(auditService).log(eq(AuditService.USER_CREATED), eq(1L), anyString());
    }

    @Test
    void createUser_duplicateEmail_throwsBusinessException() {
        CreateUserRequest req = new CreateUserRequest();
        req.setEmail("existing@university.ma");

        when(userRepository.existsByEmail("existing@university.ma")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(req))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("email existe déjà");
    }

    @Test
    void setUserStatus_disable_logsUserDisabled() {
        User user = User.builder()
                .id(2L).nom("Bob").email("bob@u.ma")
                .role(Role.ETUDIANT).actif(true).build();

        when(userRepository.findById(2L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toResponse(any())).thenReturn(
            new UserResponse(2L, "Bob", "bob@u.ma", Role.ETUDIANT, false, LocalDateTime.now()));

        userService.setUserStatus(2L, false);

        verify(auditService).log(eq(AuditService.USER_DISABLED), eq(2L), anyString());
    }
}