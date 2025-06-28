package codemuse.project.domain.user.repository;

import codemuse.project.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountId(String accountId);
    boolean existsByAccountId(String accountId);
    Optional<User> findByEmail(String email);

}
