package education.kub.backend.ce.domain.timetable_class.repository;

import education.kub.backend.ce.domain.timetable_class.entity.TimetableClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableClassRepository extends JpaRepository<TimetableClassEntity, Long> {
}
