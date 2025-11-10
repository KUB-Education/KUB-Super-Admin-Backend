package education.kub.backend.ce.domain.group.mapper;

import education.kub.backend.ce.domain.group.entity.GroupEntity;
import education.kub.backend.ce.domain.group.model.GroupFullDetailsResponse;
import education.kub.backend.ce.domain.group.model.GroupShortDetailsResponse;
import education.kub.backend.ce.domain.student.mapper.StudentMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StudentMapper.class})
public interface GroupMapper {
    GroupShortDetailsResponse toShortDetailsResponse(GroupEntity entity);

    GroupFullDetailsResponse toFullDetailsResponse(GroupEntity entity);

    List<GroupShortDetailsResponse> toShortDetailsResponseList(Iterable<GroupEntity> entities);
}
