package education.kub.backend.ce.domain.lecturer_department_position.model;

import education.kub.backend.ce.domain.lecturer_department_position.entity.LecturerDepartmentPositionEntity;

public record LecturerDepartmentPositionUpdateDto(
        Long positionId,

        LecturerDepartmentPositionEntity.Status status
) {
}