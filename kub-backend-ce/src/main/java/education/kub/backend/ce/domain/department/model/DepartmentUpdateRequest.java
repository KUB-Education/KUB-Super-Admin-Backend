package education.kub.backend.ce.domain.department.model;

import jakarta.validation.constraints.Size;

public record DepartmentUpdateRequest(
        @Size(min = 1, max = 128, message = "Department name must have length in interval [1,128]")
        String name
) {}
