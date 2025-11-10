package education.kub.backend.ce.domain.group.model;

import jakarta.validation.constraints.NotBlank;

public record GroupCreateRequest(
        @NotBlank(message = "Group name cannot be blank.")
        String name
) {
}
