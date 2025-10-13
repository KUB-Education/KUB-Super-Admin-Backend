package education.kub.backend.ce.domain.department.mapper;

import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import education.kub.backend.ce.domain.department.model.DepartmentDetailsResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentDetailsResponse toDetailsResponse(DepartmentEntity entity);

    List<DepartmentDetailsResponse> toDetailsResponseList(Iterable<DepartmentEntity> entities);
}
