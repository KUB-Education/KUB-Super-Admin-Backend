package education.kub.backend.ce.domain.lecturer_department_position.model;

import education.kub.backend.ce.domain.department.model.DepartmentDetailsResponse;
import education.kub.backend.ce.domain.lecturer_department_position.entity.LecturerDepartmentPositionEntity;
import education.kub.backend.ce.domain.position.model.PositionDto;

// DTO for LecturerDepartmentPositionEntity without reference to Lecturer
// for lecturer-related output (avoid Lecturer info duplication)
public record DepartmentPositionDto(
        Long id,

        DepartmentDetailsResponse department,

        PositionDto position,

        LecturerDepartmentPositionEntity.Status status
) {
}