package education.kub.backend.ce.domain.selected_subject_activity.repository;

import education.kub.backend.ce.domain.selected_subject_activity.domain.SelectedSubjectActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectedSubjectActivityRepository extends JpaRepository<SelectedSubjectActivityEntity, Long> {
}
