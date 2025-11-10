package education.kub.backend.ce.domain.study_field.mapper;

import education.kub.backend.ce.domain.study_field.entity.StudyFieldEntity;
import education.kub.backend.ce.domain.study_field.model.StudyFieldDetailsResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudyFieldMapper {
    StudyFieldDetailsResponse toDetailsResponse(StudyFieldEntity entity);

    List<StudyFieldDetailsResponse> toDetailsResponseList(Iterable<StudyFieldEntity> entities);
}
