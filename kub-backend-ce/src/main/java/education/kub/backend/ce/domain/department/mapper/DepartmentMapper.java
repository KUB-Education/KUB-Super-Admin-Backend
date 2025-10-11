package education.kub.backend.ce.domain.department.mapper;

import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import education.kub.backend.ce.domain.department.model.DepartmentDetailsResponse;
import education.kub.backend.ce.domain.department.model.DepartmentShortDetailsResponse;
import education.kub.backend.ce.domain.role.mapper.RoleMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface DepartmentMapper {
    DepartmentDetailsResponse toDetailsResponse(DepartmentEntity entity);

    DepartmentShortDetailsResponse toShortDetailsResponse(DepartmentEntity entity);

    List<DepartmentDetailsResponse> toDetailsResponseList(Iterable<DepartmentEntity> entities);
}
