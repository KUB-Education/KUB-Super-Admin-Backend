package education.kub.backend.ce.domain.specialty.mapper;

import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import education.kub.backend.ce.domain.specialty.model.SpecialtyDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {
    @Mapping(target = "studyFieldId", source = "studyField.id")
    SpecialtyDetailsResponse toDetailsResponse(SpecialtyEntity entity);

    @Mapping(target = "studyFieldId", source = "studyField.id")
    List<SpecialtyDetailsResponse> toDetailsResponseList(Iterable<SpecialtyEntity> entities);
}
