package education.kub.backend.ce.domain.group.model;

import jakarta.validation.constraints.NotBlank;

public record GroupUpdateRequest(
        @NotBlank(message = "Group name cannot be blank.")
        String name
) {}
