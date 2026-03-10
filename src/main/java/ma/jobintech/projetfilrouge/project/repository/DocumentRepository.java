package ma.jobintech.projetfilrouge.project.repository;

import ma.jobintech.projetfilrouge.project.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByStudentIdOrderByUploadedAtDesc(Long studentId);

    List<Document> findByProjectIdOrderByUploadedAtDesc(Long projectId);
}