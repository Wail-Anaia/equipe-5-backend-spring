package ma.jobintech.projetfilrouge.project.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.jobintech.projetfilrouge.common.enums.LivrableStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "livrables")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Livrable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_limite")
    private LocalDate dateLimite;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "livrable_status")
    @Builder.Default
    private LivrableStatus status = LivrableStatus.A_RENDRE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Builder.Default
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}