package ma.jobintech.projetfilrouge.team.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.jobintech.projetfilrouge.team.dto.request.CreateTeamRequest;
import ma.jobintech.projetfilrouge.team.dto.response.TeamResponseDTO;
import ma.jobintech.projetfilrouge.team.service.TeamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeamResponseDTO> create(@Valid @RequestBody CreateTeamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeam(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ENCADRANT')")
    public ResponseEntity<Page<TeamResponseDTO>> getAll(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(teamService.getAllTeams(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ENCADRANT')")
    public ResponseEntity<TeamResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @PostMapping("/{teamId}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeamResponseDTO> addMember(
            @PathVariable Long teamId, @PathVariable Long userId) {
        return ResponseEntity.ok(teamService.addMember(teamId, userId));
    }

    @DeleteMapping("/{teamId}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeamResponseDTO> removeMember(
            @PathVariable Long teamId, @PathVariable Long userId) {
        return ResponseEntity.ok(teamService.removeMember(teamId, userId));
    }
}