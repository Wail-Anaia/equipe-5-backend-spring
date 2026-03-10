package ma.jobintech.projetfilrouge.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.jobintech.projetfilrouge.audit.AuditService;
import ma.jobintech.projetfilrouge.exception.types.BusinessException;
import ma.jobintech.projetfilrouge.project.dto.request.CreateProjectRequest;
import ma.jobintech.projetfilrouge.project.dto.request.UpdateProjectRequest;
import ma.jobintech.projetfilrouge.project.dto.response.ProjectResponseDTO;
import ma.jobintech.projetfilrouge.project.entity.Project;
import ma.jobintech.projetfilrouge.project.mapper.ProjectMapper;
import ma.jobintech.projetfilrouge.project.repository.ProjectRepository;
import ma.jobintech.projetfilrouge.team.entity.Team;
import ma.jobintech.projetfilrouge.team.repository.TeamRepository;
import ma.jobintech.projetfilrouge.user.entity.User;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository    userRepository;
    private final TeamRepository    teamRepository;
    private final ProjectMapper     projectMapper;
    private final AuditService      auditService;

    @Override
    @Transactional
    public ProjectResponseDTO createProject(CreateProjectRequest request) {
        Project.ProjectBuilder builder = Project.builder()
                .titre(request.getTitre())
                .description(request.getDescription())
                .technologies(request.getTechnologies());

        if (request.getStatus() != null) {
            builder.status(request.getStatus());
        }

        if (request.getEncadrantId() != null) {
            User encadrant = userRepository.findById(request.getEncadrantId())
                .orElseThrow(() -> new BusinessException("Encadrant introuvable : " + request.getEncadrantId()));
            builder.encadrant(encadrant);
        }

        if (request.getTeamId() != null) {
            Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new BusinessException("Équipe introuvable : " + request.getTeamId()));
            builder.team(team);
        }

        Project saved = projectRepository.save(builder.build());

        auditService.log(
            AuditService.PROJECT_CREATED, null, null,
            "Projet créé : " + saved.getTitre() + " (id=" + saved.getId() + ")"
        );

        log.info("Projet créé — id={} titre={}", saved.getId(), saved.getTitre());
        return projectMapper.toDto(saved);
    }

    @Override
    public ProjectResponseDTO getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(projectMapper::toDto)
                .orElseThrow(() -> new BusinessException("Projet introuvable : " + id));
    }

    @Override
    public Page<ProjectResponseDTO> getAllProjects(Pageable pageable) {
        return projectRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(projectMapper::toDto);
    }

    @Override
    @Transactional
    public ProjectResponseDTO updateProject(Long id, UpdateProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Projet introuvable : " + id));

        if (request.getTitre()        != null) project.setTitre(request.getTitre());
        if (request.getDescription()  != null) project.setDescription(request.getDescription());
        if (request.getTechnologies() != null) project.setTechnologies(request.getTechnologies());
        if (request.getStatus()       != null) project.setStatus(request.getStatus());

        if (request.getEncadrantId() != null) {
            User encadrant = userRepository.findById(request.getEncadrantId())
                .orElseThrow(() -> new BusinessException("Encadrant introuvable : " + request.getEncadrantId()));
            project.setEncadrant(encadrant);
        }

        if (request.getTeamId() != null) {
            Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new BusinessException("Équipe introuvable : " + request.getTeamId()));
            project.setTeam(team);
        }

        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new BusinessException("Projet introuvable : " + id);
        }
        projectRepository.deleteById(id);
    }

    @Override
    public List<ProjectResponseDTO> getProjectsByEncadrant(Long encadrantId) {
        return projectRepository.findByEncadrantId(encadrantId)
                .stream().map(projectMapper::toDto).toList();
    }

    @Override
    public List<ProjectResponseDTO> getProjectsByStudent(Long studentId) {
        return projectRepository.findByStudentId(studentId)
                .stream().map(projectMapper::toDto).toList();
    }
}