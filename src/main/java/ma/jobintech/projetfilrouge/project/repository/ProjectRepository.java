package ma.jobintech.projetfilrouge.project.repository;

import ma.jobintech.projetfilrouge.common.enums.ProjectStatus;
import ma.jobintech.projetfilrouge.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    long countByStatus(ProjectStatus status);

    List<Project> findByEncadrantId(Long encadrantId);

    @Query("SELECT p FROM Project p JOIN p.team.members m WHERE m.id = :studentId")
    List<Project> findByStudentId(@Param("studentId") Long studentId);

    Page<Project> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
//    List<Project> findTop5ByOrderByUpdatedAtDesc();
}