package education.kub.backend.ce.domain.lecturer.mapper;

import education.kub.backend.ce.domain.academic_title.mapper.AcademicTitleMapper;
import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.lecturer.model.LecturerDetailsResponse;
import education.kub.backend.ce.domain.lecturer_department_position.mapper.LecturerDepartmentPositionMapper;
import education.kub.backend.ce.domain.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, LecturerDepartmentPositionMapper.class, AcademicTitleMapper.class})
public interface LecturerMapper {
    @Mapping(target = "departmentPositions", source = "lecturerDepartmentPositions")
    LecturerDetailsResponse toDetailsResponse(LecturerEntity entity);

    @Mapping(target = "departmentPositions", source = "lecturerDepartmentPositions")
    List<LecturerDetailsResponse> toDetailsResponseList(Iterable<LecturerEntity> entities);
}
