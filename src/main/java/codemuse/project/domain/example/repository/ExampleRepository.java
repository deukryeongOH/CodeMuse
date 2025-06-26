package codemuse.project.domain.example.repository;

import codemuse.project.domain.example.entity.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {
}
