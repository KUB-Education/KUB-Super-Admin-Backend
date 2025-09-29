package education.kub.backend.ce.domain.selected_subject_activity_group.repository;

import education.kub.backend.ce.domain.selected_subject_activity_group.domain.SelectedSubjectActivityGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectedSubjectActivityGroupRepository
        extends JpaRepository<SelectedSubjectActivityGroupEntity, Long> {
}
