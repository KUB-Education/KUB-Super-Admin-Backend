package education.kub.backend.ce.domain.study_field.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record StudyFieldUpdateRequest(
        @Pattern(regexp = "^(?!\\s*$).+",
                message = "Code must contain at least one non-whitespace character")
        @Size(min = 1, max = 32, message = "Code must have length in interval [1,32]")
        String code,

        @Pattern(regexp = "^(?!\\s*$).+",
                message = "Name must contain at least one non-whitespace character")
        @Size(min = 1, max = 128, message = "Name must have length in interval [1,128]")
        String name
) {}
