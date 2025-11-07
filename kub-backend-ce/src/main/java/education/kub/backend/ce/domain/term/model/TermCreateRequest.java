package education.kub.backend.ce.domain.term.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TermCreateRequest(
        @NotNull(message = "Educational program ID must not be null")
        Long educationalProgramId,

        @NotNull(message = "Term number must not be null")
        @Min(value = 0, message = "Term number must be positive or zero")
        Short number
) {}
