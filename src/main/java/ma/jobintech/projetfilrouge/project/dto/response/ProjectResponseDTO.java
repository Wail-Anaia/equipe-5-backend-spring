package ma.jobintech.projetfilrouge.project.dto.response;

import lombok.*;
import ma.jobintech.projetfilrouge.common.enums.ProjectStatus;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectResponseDTO {
    private Long          id;
    private String        titre;
    private String        description;
    private String        technologies;
    private ProjectStatus status;
    private EncadrantInfo encadrant;
    private TeamInfo      team;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class EncadrantInfo {
        private Long   id;
        private String nom;
        private String email;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class TeamInfo {
        private Long   id;
        private String nom;
    }
}