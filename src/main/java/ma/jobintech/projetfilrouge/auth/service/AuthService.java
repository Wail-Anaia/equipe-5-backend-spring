package ma.jobintech.projetfilrouge.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.jobintech.projetfilrouge.audit.AuditService;
import ma.jobintech.projetfilrouge.auth.dto.AuthResponse;
import ma.jobintech.projetfilrouge.auth.dto.LoginRequest;
import ma.jobintech.projetfilrouge.security.jwt.JwtService;
import ma.jobintech.projetfilrouge.user.entity.User;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService    userDetailsService;
    private final JwtService            jwtService;
    private final UserRepository        userRepository;
    private final AuditService          auditService;

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (DisabledException ex) {
            auditService.log(AuditService.LOGIN_FAILED, null, null,
                "Compte désactivé : " + request.getEmail());
            throw new DisabledException("Ce compte est désactivé");
        } catch (AuthenticationException ex) {
            auditService.log(AuditService.LOGIN_FAILED, null, null,
                "Tentative échouée pour : " + request.getEmail());
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        String token = jwtService.generateToken(userDetails, Map.of(
            "role", user.getRole().name(),
            "nom",  user.getNom()
        ));

        auditService.log(AuditService.LOGIN_SUCCESS, user.getId(), user.getId(),
            "Login réussi : " + user.getEmail());

        log.info("Login réussi — email={}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .nom(user.getNom())
                .role(user.getRole())
                .build();
    }
}