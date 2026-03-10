package ma.jobintech.projetfilrouge.user.service;

import ma.jobintech.projetfilrouge.user.dto.request.ChangePasswordRequest;
import ma.jobintech.projetfilrouge.user.dto.request.CreateUserRequest;
import ma.jobintech.projetfilrouge.user.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDTO createUser(CreateUserRequest request);
    UserResponseDTO getUserById(Long id);
    Page<UserResponseDTO> getAllUsers(Pageable pageable);
    UserResponseDTO setUserStatus(Long id, boolean actif, Long performedById);
    void changePassword(Long userId, ChangePasswordRequest request);
}