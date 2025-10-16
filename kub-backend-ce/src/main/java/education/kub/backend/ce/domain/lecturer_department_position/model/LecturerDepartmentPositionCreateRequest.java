package education.kub.backend.ce.domain.lecturer_department_position.model;

import education.kub.backend.ce.domain.lecturer_department_position.entity.LecturerDepartmentPositionEntity;
import jakarta.validation.constraints.NotNull;

public record LecturerDepartmentPositionCreateRequest(
        @NotNull(message = "lecturer ID can't be null")
        Long lecturerId,

        @NotNull(message = "department ID can't be null")
        Long departmentId,

        @NotNull(message = "position ID can't be null")
        Long positionId
) {
}
