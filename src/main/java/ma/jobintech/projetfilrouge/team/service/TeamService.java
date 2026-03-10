package ma.jobintech.projetfilrouge.team.service;

import ma.jobintech.projetfilrouge.team.dto.request.CreateTeamRequest;
import ma.jobintech.projetfilrouge.team.dto.response.TeamResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamService {
    TeamResponseDTO createTeam(CreateTeamRequest request);
    TeamResponseDTO getTeamById(Long id);
    Page<TeamResponseDTO> getAllTeams(Pageable pageable);
    TeamResponseDTO addMember(Long teamId, Long userId);
    TeamResponseDTO removeMember(Long teamId, Long userId);
}