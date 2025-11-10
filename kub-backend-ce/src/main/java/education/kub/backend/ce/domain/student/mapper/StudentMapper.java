package education.kub.backend.ce.domain.student.mapper;

import education.kub.backend.ce.domain.student.entity.StudentEntity;
import education.kub.backend.ce.domain.student.model.StudentShortDetailsResponse;
import education.kub.backend.ce.domain.user.mapper.UserMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class})
public interface StudentMapper {
    StudentShortDetailsResponse toShortDetailsResponse(StudentEntity entity);

    List<StudentShortDetailsResponse> toShortDetailsResponseList(Iterable<StudentEntity> entities);
}
