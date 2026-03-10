package ma.jobintech.projetfilrouge.team.repository;

import ma.jobintech.projetfilrouge.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t FROM Team t JOIN t.members m WHERE m.id = :userId")
    Optional<Team> findByMemberId(@Param("userId") Long userId);
}