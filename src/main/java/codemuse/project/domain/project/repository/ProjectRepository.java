package codemuse.project.domain.project.repository;

import codemuse.project.domain.project.entity.Project;
import codemuse.project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.lang.ScopedValue;
import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
