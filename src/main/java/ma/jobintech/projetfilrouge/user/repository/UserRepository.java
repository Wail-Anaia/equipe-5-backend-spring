package ma.jobintech.projetfilrouge.user.repository;

import ma.jobintech.projetfilrouge.common.enums.Role;
import ma.jobintech.projetfilrouge.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByRole(Role role);

    long countByActifTrue();

    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
           "(:role IS NULL OR u.role = :role) AND " +
           "(:search IS NULL OR LOWER(u.nom) LIKE LOWER(CONCAT('%',:search,'%')) " +
           "   OR LOWER(u.email) LIKE LOWER(CONCAT('%',:search,'%')))")
    Page<User> findWithFilters(@Param("role") Role role,
                               @Param("search") String search,
                               Pageable pageable);
}