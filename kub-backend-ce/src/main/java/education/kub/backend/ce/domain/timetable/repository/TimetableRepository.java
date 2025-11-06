package education.kub.backend.ce.domain.timetable.repository;

import education.kub.backend.ce.domain.timetable.entity.TimetableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableRepository extends JpaRepository<TimetableEntity, Long> {
    boolean existsByName(String name);
}
