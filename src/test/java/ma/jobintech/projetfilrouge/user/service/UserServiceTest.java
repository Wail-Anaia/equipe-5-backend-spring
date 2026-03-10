package ma.jobintech.projetfilrouge.user.service;

import ma.jobintech.projetfilrouge.audit.AuditService;
import ma.jobintech.projetfilrouge.common.enums.Role;
import ma.jobintech.projetfilrouge.exception.types.BusinessException;
import ma.jobintech.projetfilrouge.user.dto.request.CreateUserRequest;
import ma.jobintech.projetfilrouge.user.dto.response.UserResponseDTO;
import ma.jobintech.projetfilrouge.user.entity.User;
import ma.jobintech.projetfilrouge.user.mapper.UserMapper;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks UserServiceImpl userService;
    @Mock UserRepository  userRepository;
    @Mock UserMapper      userMapper;
    @Mock PasswordEncoder passwordEncoder;
    @Mock AuditService    auditService;

    @Test
    void createUser_newEmail_returnsDto() {
        CreateUserRequest req = new CreateUserRequest("Alice", "alice@test.ma", "password123", Role.ETUDIANT);
        User saved = User.builder().id(1L).nom("Alice").email("alice@test.ma").role(Role.ETUDIANT).actif(true).build();
        UserResponseDTO dto = UserResponseDTO.builder().id(1L).nom("Alice").email("alice@test.ma").build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$hashed");
        when(userRepository.save(any())).thenReturn(saved);
        when(userMapper.toDto(saved)).thenReturn(dto);

        UserResponseDTO result = userService.createUser(req);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNom()).isEqualTo("Alice");
        verify(auditService).log(eq(AuditService.USER_CREATED), any(), any(), anyString());
    }

    @Test
    void createUser_duplicateEmail_throwsBusinessException() {
        CreateUserRequest req = new CreateUserRequest("Bob", "bob@test.ma", "pass", Role.ETUDIANT);
        when(userRepository.existsByEmail("bob@test.ma")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(req))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("bob@test.ma");
    }

    @Test
    void getUserById_notFound_throwsUserNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
            .isInstanceOf(ma.jobintech.projetfilrouge.exception.types.UserNotFoundException.class);
    }
}