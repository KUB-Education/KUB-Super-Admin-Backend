package education.kub.backend.ce.domain.term.repository;

import education.kub.backend.ce.domain.term.domain.TermEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<TermEntity, Long> {
}
