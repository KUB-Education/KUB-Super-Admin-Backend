package education.kub.backend.ce.domain.lecturer.model;

import jakarta.validation.constraints.NotNull;

public record LecturerAddDepartmentPositionRequest(
        @NotNull(message = "department ID can't be null")
        Long departmentId,

        @NotNull(message = "position ID can't be null")
        Long positionId
) {
}
