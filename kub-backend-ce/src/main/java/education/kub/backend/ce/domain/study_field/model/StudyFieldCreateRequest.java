package education.kub.backend.ce.domain.study_field.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudyFieldCreateRequest(
        @NotNull(message = "Code can't be null")
        @NotBlank(message = "Code can't be blank")
        @Size(min = 1, max = 32, message = "Code must have length in interval [1,32]")
        String code,

        @NotNull(message = "Name can't be null")
        @NotBlank(message = "Name can't be blank")
        @Size(min = 1, max = 128, message = "Name must have length in interval [1,128]")
        String name
) {}
