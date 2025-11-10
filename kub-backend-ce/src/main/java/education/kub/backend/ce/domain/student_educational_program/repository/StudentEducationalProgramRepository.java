package education.kub.backend.ce.domain.student_educational_program.repository;

import education.kub.backend.ce.domain.student_educational_program.entity.StudentEducationalProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentEducationalProgramRepository extends JpaRepository<StudentEducationalProgramEntity, Long> {
}
