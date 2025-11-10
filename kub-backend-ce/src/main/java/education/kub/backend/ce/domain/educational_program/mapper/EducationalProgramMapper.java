package education.kub.backend.ce.domain.educational_program.mapper;

import education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity;
import education.kub.backend.ce.domain.educational_program.model.EducationalProgramDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EducationalProgramMapper {
    @Mapping(target = "specialtyId", source = "specialty.id")
    EducationalProgramDetailsResponse toDetailsResponse(EducationalProgramEntity entity);

    @Mapping(target = "specialtyId", source = "specialty.id")
    List<EducationalProgramDetailsResponse> toDetailsResponseList(Iterable<EducationalProgramEntity> entities);
}
