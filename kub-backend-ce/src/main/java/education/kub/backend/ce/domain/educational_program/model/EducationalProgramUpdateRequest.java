package education.kub.backend.ce.domain.educational_program.model;

import education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EducationalProgramUpdateRequest(
        Long specialtyId,

        @Pattern(regexp = "^(?!\\s*$).+",
                message = "Name must contain at least one non-whitespace character")
        @Size(min = 1, max = 128, message = "Name must have length in interval [1,128]")
        String name,

        EducationalProgramEntity.DegreeType degreeType,

        EducationalProgramEntity.StudyForm studyForm,

        @Min(value = 0, message = "Duration must be not negative")
        Short duration
) {
}
