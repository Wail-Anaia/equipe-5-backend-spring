package ma.jobintech.projetfilrouge.auth.service;

import ma.jobintech.projetfilrouge.audit.AuditService;
import ma.jobintech.projetfilrouge.user.dto.request.LoginRequest;
import ma.jobintech.projetfilrouge.user.dto.response.AuthResponse;
import ma.jobintech.projetfilrouge.user.entity.User;
import ma.jobintech.projetfilrouge.exception.UserNotFoundException;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import ma.jobintech.projetfilrouge.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AuditService auditService;

    public AuthResponse login(LoginRequest request) {
        try {
            // Délègue à Spring Security (vérifie password + actif via UserDetails)
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );
        } catch (BadCredentialsException e) {
            auditService.log(AuditService.LOGIN_FAILED, null,
                "Tentative échouée pour : " + request.getEmail());
            throw e;
        } catch (DisabledException e) {
            auditService.log(AuditService.LOGIN_FAILED, null,
                "Compte désactivé : " + request.getEmail());
            throw e;
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        Map<String, Object> claims = Map.of(
            "role", user.getRole().name(),
            "nom", user.getNom()
        );

        String token = jwtService.generateToken(userDetails, claims);

        auditService.log(AuditService.LOGIN_SUCCESS, user.getId(),
            "Login réussi : " + user.getEmail());

        return new AuthResponse(
            token,
            user.getEmail(),
            user.getNom(),
            user.getRole(),
            jwtService.getExpirationMs()
        );
    }
}