package ma.jobintech.projetfilrouge.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.jobintech.projetfilrouge.user.dto.CreateUserRequest;
import ma.jobintech.projetfilrouge.user.dto.UserResponseDTO;
import ma.jobintech.projetfilrouge.user.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // CA-5 : accès ADMIN uniquement
    public ResponseEntity<UserResponseDTO> create(
            @Valid @RequestBody CreateUserRequest dto) {

        return ResponseEntity
            .status(HttpStatus.CREATED) // CA-4 : HTTP 201
            .body(service.createUser(dto));
    }
}