package education.kub.backend.ce.domain.specialty.model;

import education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SpecialtyEducationalProgramCreateRequest(
        @NotBlank(message = "Name can't be blank")
        @Size(min = 1, max = 128, message = "Name must have length in interval [1,128]")
        String name,

        @NotNull(message = "Degree type can't be null")
        EducationalProgramEntity.DegreeType degreeType,

        @NotNull(message = "Study form can't be null")
        EducationalProgramEntity.StudyForm studyForm,

        @NotNull(message = "Duration can't be null")
        @Min(value = 0, message = "Duration must be not negative")
        Short duration
) {
}
