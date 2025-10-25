package education.kub.backend.ce.domain.lecturer_department_position.mapper;

import education.kub.backend.ce.domain.department.mapper.DepartmentMapper;
import education.kub.backend.ce.domain.lecturer_department_position.entity.LecturerDepartmentPositionEntity;
import education.kub.backend.ce.domain.lecturer_department_position.model.DepartmentPositionDto;
import education.kub.backend.ce.domain.lecturer_department_position.model.LecturerDepartmentPositionDto;
import education.kub.backend.ce.domain.position.mapper.PositionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, PositionMapper.class})
public interface LecturerDepartmentPositionMapper {
    DepartmentPositionDto toDepartmentPositionDto(LecturerDepartmentPositionEntity entity);

    List<DepartmentPositionDto> toDepartmentPositionDtoList(Iterable<LecturerDepartmentPositionEntity> entities);

    @Mapping(target = "lecturerId", source = "lecturer.id")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "positionId", source = "position.id")
    LecturerDepartmentPositionDto toDto(LecturerDepartmentPositionEntity entity);

    @Mapping(target = "lecturerId", source = "lecturer.id")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "positionId", source = "position.id")
    List<LecturerDepartmentPositionDto> toDtoList(Iterable<LecturerDepartmentPositionEntity> entities);
}
