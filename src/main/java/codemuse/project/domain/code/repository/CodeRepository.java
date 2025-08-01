package codemuse.project.domain.code.repository;

import codemuse.project.domain.code.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
}
