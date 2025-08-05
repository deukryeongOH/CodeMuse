package codemuse.project.domain.learn.repository;

import codemuse.project.domain.learn.entity.Learn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LearnRepository extends JpaRepository<Learn, Long> {
}
