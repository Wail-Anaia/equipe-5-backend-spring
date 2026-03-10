package ma.jobintech.projetfilrouge.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.jobintech.projetfilrouge.user.dto.request.ChangePasswordRequest;
import ma.jobintech.projetfilrouge.user.dto.response.UserResponseDTO;
import ma.jobintech.projetfilrouge.user.mapper.UserMapper;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import ma.jobintech.projetfilrouge.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final UserMapper     userMapper;
    private final UserService    userService;

    // ── Récupérer son propre profil ───────────────────
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(
            @AuthenticationPrincipal UserDetails principal) {
        return userRepository.findByEmail(principal.getUsername())
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── Changer son propre mot de passe ───────────────
    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        Long userId = userRepository.findByEmail(principal.getUsername())
                .orElseThrow().getId();
        userService.changePassword(userId, request);
        return ResponseEntity.noContent().build();
    }
}