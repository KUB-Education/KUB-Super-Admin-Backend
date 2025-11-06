package education.kub.backend.ce.domain.timetable.mapper;

import education.kub.backend.ce.domain.timetable.entity.TimetableEntity;
import education.kub.backend.ce.domain.timetable.model.TimetableDetailsResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimetableMapper {
    TimetableDetailsResponse toDetailsResponse(TimetableEntity entity);

    List<TimetableDetailsResponse> toDetailsResponseList(Iterable<TimetableEntity> entity);
}
