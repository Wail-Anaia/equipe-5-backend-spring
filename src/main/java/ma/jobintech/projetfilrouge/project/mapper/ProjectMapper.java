package ma.jobintech.projetfilrouge.project.mapper;

import ma.jobintech.projetfilrouge.project.dto.response.ProjectResponseDTO;
import ma.jobintech.projetfilrouge.project.entity.Project;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "encadrant", expression = "java(project.getEncadrant() == null ? null : " +
        "ProjectResponseDTO.EncadrantInfo.builder()" +
        ".id(project.getEncadrant().getId())" +
        ".nom(project.getEncadrant().getNom())" +
        ".email(project.getEncadrant().getEmail())" +
        ".build())")
    @Mapping(target = "team", expression = "java(project.getTeam() == null ? null : " +
        "ProjectResponseDTO.TeamInfo.builder()" +
        ".id(project.getTeam().getId())" +
        ".nom(project.getTeam().getNom())" +
        ".build())")
    ProjectResponseDTO toDto(Project project);
}