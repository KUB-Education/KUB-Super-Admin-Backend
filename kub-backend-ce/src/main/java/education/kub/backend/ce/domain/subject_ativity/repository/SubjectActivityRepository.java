package education.kub.backend.ce.domain.subject_ativity.repository;

import education.kub.backend.ce.domain.subject_ativity.domain.SubjectActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectActivityRepository extends JpaRepository<SubjectActivityEntity, Long> {
}
