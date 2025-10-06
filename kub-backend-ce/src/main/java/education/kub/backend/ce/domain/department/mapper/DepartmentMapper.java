package education.kub.backend.ce.domain.department.mapper;

import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import education.kub.backend.ce.domain.department.model.DepartmentResponse;
import education.kub.backend.ce.domain.role.mapper.RoleMapper;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface DepartmentMapper {
    DepartmentResponse toDepartmentResponse(DepartmentEntity entity);

    List<DepartmentResponse> toDepartmentResponseList(Iterable<DepartmentEntity> entities);

    Optional<DepartmentResponse> toDepartmentResponseOptional(Optional<DepartmentEntity> entity);
}
