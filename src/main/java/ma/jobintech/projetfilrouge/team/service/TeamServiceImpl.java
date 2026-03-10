package ma.jobintech.projetfilrouge.team.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.jobintech.projetfilrouge.exception.types.BusinessException;
import ma.jobintech.projetfilrouge.team.dto.request.CreateTeamRequest;
import ma.jobintech.projetfilrouge.team.dto.response.TeamResponseDTO;
import ma.jobintech.projetfilrouge.team.entity.Team;
import ma.jobintech.projetfilrouge.team.repository.TeamRepository;
import ma.jobintech.projetfilrouge.user.entity.User;
import ma.jobintech.projetfilrouge.user.mapper.UserMapper;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserMapper     userMapper;

    @Override
    @Transactional
    public TeamResponseDTO createTeam(CreateTeamRequest request) {
        Team.TeamBuilder builder = Team.builder().nom(request.getNom());

        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            Set<User> members = new HashSet<>(userRepository.findAllById(request.getMemberIds()));
            builder.members(members);
        }

        Team saved = teamRepository.save(builder.build());
        log.info("Équipe créée — id={} nom={}", saved.getId(), saved.getNom());
        return toDto(saved);
    }

    @Override
    public TeamResponseDTO getTeamById(Long id) {
        return teamRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new BusinessException("Équipe introuvable : " + id));
    }

    @Override
    public Page<TeamResponseDTO> getAllTeams(Pageable pageable) {
        return teamRepository.findAll(pageable).map(this::toDto);
    }

    @Override
    @Transactional
    public TeamResponseDTO addMember(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException("Équipe introuvable : " + teamId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Utilisateur introuvable : " + userId));
        team.getMembers().add(user);
        return toDto(teamRepository.save(team));
    }

    @Override
    @Transactional
    public TeamResponseDTO removeMember(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException("Équipe introuvable : " + teamId));
        team.getMembers().removeIf(m -> m.getId().equals(userId));
        return toDto(teamRepository.save(team));
    }

    private TeamResponseDTO toDto(Team team) {
        return TeamResponseDTO.builder()
                .id(team.getId())
                .nom(team.getNom())
                .createdAt(team.getCreatedAt())
                .members(team.getMembers().stream()
                        .map(userMapper::toDto)
                        .collect(Collectors.toSet()))
                .build();
    }
}