package education.kub.backend.ce.domain.educational_program.repository;

import education.kub.backend.ce.domain.educational_program.domain.EducationalProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationalProgramRepository extends JpaRepository<EducationalProgramEntity, Long> {
}
