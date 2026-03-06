package ma.jobintech.projetfilrouge.user.service;

import ma.jobintech.projetfilrouge.audit.AuditService;
import ma.jobintech.projetfilrouge.user.dto.request.CreateUserRequest;
import ma.jobintech.projetfilrouge.user.dto.response.PageResponse;
import ma.jobintech.projetfilrouge.user.dto.response.UserResponse;
import ma.jobintech.projetfilrouge.user.entity.User;
import ma.jobintech.projetfilrouge.exception.BusinessException;
import ma.jobintech.projetfilrouge.exception.UserNotFoundException;
import ma.jobintech.projetfilrouge.user.mapper.UserMapper;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse createUser(CreateUserRequest dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Un compte avec cet email existe déjà.");
        }

        User user = User.builder()
                .nom(dto.getNom())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .actif(true)
                .build();

        User saved = userRepository.save(user);

        auditService.log(AuditService.USER_CREATED, saved.getId(),
            "Compte créé : " + saved.getEmail() + " | Rôle : " + saved.getRole());

        return userMapper.toResponse(saved);
    }

    public UserResponse getUserById(Long id) {
        return userMapper.toResponse(findUserOrThrow(id));
    }

    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAllByOrderByCreatedAtDesc(pageable);

        return new PageResponse<>(
            userMapper.toResponseList(userPage.getContent()),
            userPage.getNumber(),
            userPage.getSize(),
            userPage.getTotalElements(),
            userPage.getTotalPages()
        );
    }

    @Transactional
    public UserResponse setUserStatus(Long userId, boolean actif) {
        User user = findUserOrThrow(userId);
        user.setActif(actif);
        User saved = userRepository.save(user);

        String action = actif ? AuditService.USER_ENABLED : AuditService.USER_DISABLED;
        auditService.log(action, saved.getId(),
            "Statut modifié : " + saved.getEmail() + " → actif=" + actif);

        return userMapper.toResponse(saved);
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable (id=" + id + ")"));
    }
}