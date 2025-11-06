package education.kub.backend.ce.domain.subject_activity.repository;

import education.kub.backend.ce.domain.subject_activity.entity.SubjectActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectActivityRepository extends JpaRepository<SubjectActivityEntity, Long> {
}
