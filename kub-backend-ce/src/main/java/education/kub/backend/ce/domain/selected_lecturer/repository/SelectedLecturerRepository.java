package education.kub.backend.ce.domain.selected_lecturer.repository;

import education.kub.backend.ce.domain.selected_lecturer.domain.SelectedLecturerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectedLecturerRepository extends JpaRepository<SelectedLecturerEntity, Long> {
}
