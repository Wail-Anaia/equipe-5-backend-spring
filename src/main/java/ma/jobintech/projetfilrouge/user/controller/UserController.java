package ma.jobintech.projetfilrouge.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.jobintech.projetfilrouge.user.dto.request.ChangePasswordRequest;
import ma.jobintech.projetfilrouge.user.dto.request.CreateUserRequest;
import ma.jobintech.projetfilrouge.user.dto.request.UpdateUserStatusRequest;
import ma.jobintech.projetfilrouge.user.dto.response.UserResponseDTO;
import ma.jobintech.projetfilrouge.user.entity.User;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import ma.jobintech.projetfilrouge.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        // Récupérer l'ID de l'admin connecté via email
        return ResponseEntity.ok(userService.setUserStatus(id, request.getActif(), null));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UserDetails principal) {

        String email = principal.getUsername();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.changePassword(user.getId(), request);

        return ResponseEntity.noContent().build();
    }
}