package ma.jobintech.projetfilrouge.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.jobintech.projetfilrouge.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email); // CA-2 : unicité email

    Optional<User> findByEmail(String email); // usage Auth A1
}