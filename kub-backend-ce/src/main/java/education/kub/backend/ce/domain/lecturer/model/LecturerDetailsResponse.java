package education.kub.backend.ce.domain.lecturer.model;

import education.kub.backend.ce.domain.academic_title.model.AcademicTitleDto;
import education.kub.backend.ce.domain.lecturer_department_position.model.DepartmentPositionDto;
import education.kub.backend.ce.domain.user.model.UserDetailsResponse;

import java.util.List;

public record LecturerDetailsResponse(
        Long id,

        UserDetailsResponse user,

        List<DepartmentPositionDto> departmentPositions,

        List<AcademicTitleDto> academicTitles
){
}