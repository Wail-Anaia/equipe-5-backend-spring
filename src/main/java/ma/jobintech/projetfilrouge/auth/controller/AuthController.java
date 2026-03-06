package ma.jobintech.projetfilrouge.auth.controller;

import ma.jobintech.projetfilrouge.user.dto.request.LoginRequest;
import ma.jobintech.projetfilrouge.user.dto.response.AuthResponse;
import ma.jobintech.projetfilrouge.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}