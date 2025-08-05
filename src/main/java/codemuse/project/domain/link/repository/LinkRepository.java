package codemuse.project.domain.link.repository;

import codemuse.project.domain.link.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
}
