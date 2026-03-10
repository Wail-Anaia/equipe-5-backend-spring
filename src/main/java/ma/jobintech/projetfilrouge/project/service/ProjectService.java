package ma.jobintech.projetfilrouge.project.service;

import ma.jobintech.projetfilrouge.project.dto.request.CreateProjectRequest;
import ma.jobintech.projetfilrouge.project.dto.request.UpdateProjectRequest;
import ma.jobintech.projetfilrouge.project.dto.response.ProjectResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    ProjectResponseDTO createProject(CreateProjectRequest request);
    ProjectResponseDTO getProjectById(Long id);
    Page<ProjectResponseDTO> getAllProjects(Pageable pageable);
    ProjectResponseDTO updateProject(Long id, UpdateProjectRequest request);
    void deleteProject(Long id);
    List<ProjectResponseDTO> getProjectsByEncadrant(Long encadrantId);
    List<ProjectResponseDTO> getProjectsByStudent(Long studentId);
}