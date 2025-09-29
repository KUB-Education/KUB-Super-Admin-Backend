package education.kub.backend.ce.domain.subject.repository;

import education.kub.backend.ce.domain.subject.domain.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
}
