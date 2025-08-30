package education.kub.backend.ce.domain.role.mapper;

import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.role.model.RoleDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto toDto(RoleEntity role);

    List<RoleDto> toDtoList(Iterable<RoleEntity> entities);
}
