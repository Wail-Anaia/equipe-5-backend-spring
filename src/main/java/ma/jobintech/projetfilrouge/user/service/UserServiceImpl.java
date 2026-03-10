package ma.jobintech.projetfilrouge.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.jobintech.projetfilrouge.audit.AuditService;
import ma.jobintech.projetfilrouge.exception.types.BusinessException;
import ma.jobintech.projetfilrouge.user.dto.request.ChangePasswordRequest;
import ma.jobintech.projetfilrouge.user.dto.request.CreateUserRequest;
import ma.jobintech.projetfilrouge.user.dto.response.UserResponseDTO;
import ma.jobintech.projetfilrouge.user.entity.User;
import ma.jobintech.projetfilrouge.user.mapper.UserMapper;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository  userRepository;
    private final UserMapper      userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditService    auditService;

    @Override
    @Transactional
    public UserResponseDTO createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Un compte avec cet email existe déjà : " + request.getEmail());
        }

        User user = User.builder()
                .nom(request.getNom())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .actif(true)
                .build();

        User saved = userRepository.save(user);

        auditService.log(
            AuditService.USER_CREATED,
            saved.getId(),
            null,
            String.format("Compte créé : %s | Rôle : %s", saved.getEmail(), saved.getRole())
        );

        log.info("Nouveau compte créé — email={} role={}", saved.getEmail(), saved.getRole());
        return userMapper.toDto(saved);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ma.jobintech.projetfilrouge.exception.types.UserNotFoundException(id));
    }

    @Override
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional
    public UserResponseDTO setUserStatus(Long id, boolean actif, Long performedById) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ma.jobintech.projetfilrouge.exception.types.UserNotFoundException(id));

        user.setActif(actif);
        userRepository.save(user);

        String action = actif ? AuditService.USER_ENABLED : AuditService.USER_DISABLED;
        auditService.log(action, id, performedById,
            String.format("Statut modifié : %s → actif=%b", user.getEmail(), actif));

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ma.jobintech.projetfilrouge.exception.types.UserNotFoundException(userId));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException("Mot de passe actuel incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}