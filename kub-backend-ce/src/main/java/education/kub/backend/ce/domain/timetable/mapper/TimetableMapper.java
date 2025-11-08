package education.kub.backend.ce.domain.timetable.mapper;

import education.kub.backend.ce.domain.timetable.entity.TimetableEntity;
import education.kub.backend.ce.domain.timetable.model.TimetableDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimetableMapper {
    @Mapping(target = "group", source = "group.id")
    TimetableDetailsResponse toDetailsResponse(TimetableEntity entity);

    @Mapping(target = "group", source = "group.id")
    List<TimetableDetailsResponse> toDetailsResponseList(Iterable<TimetableEntity> entity);
}
