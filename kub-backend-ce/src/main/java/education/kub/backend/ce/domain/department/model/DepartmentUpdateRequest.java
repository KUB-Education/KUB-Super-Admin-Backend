package education.kub.backend.ce.domain.department.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DepartmentUpdateRequest(
        @NotNull(message = "Department name can't be null")
        @NotBlank(message = "Department name can't be blank")
        @Size(min = 1, max = 128, message = "Department name must have length in interval [1,128]")
        String name
) {}
