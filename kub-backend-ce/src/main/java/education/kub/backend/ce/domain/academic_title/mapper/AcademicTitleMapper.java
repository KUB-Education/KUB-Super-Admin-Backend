package education.kub.backend.ce.domain.academic_title.mapper;


import education.kub.backend.ce.domain.academic_title.entity.AcademicTitleEntity;
import education.kub.backend.ce.domain.academic_title.model.AcademicTitleDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AcademicTitleMapper {
    AcademicTitleDto toDto(AcademicTitleEntity entity);

    List<AcademicTitleDto> toDtoList(Iterable<AcademicTitleEntity> entities);
}
