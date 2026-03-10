package ma.jobintech.projetfilrouge.project.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.jobintech.projetfilrouge.common.enums.ProjectStatus;
import ma.jobintech.projetfilrouge.team.entity.Team;
import ma.jobintech.projetfilrouge.user.entity.User;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String technologies;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "project_status", nullable = false)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.EN_ATTENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encadrant_id")
    private User encadrant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder.Default
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}