package education.kub.backend.ce.domain.role.mapper;

import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.role.model.RoleDetailsResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDetailsResponse toDetailsResponse(RoleEntity role);

    List<RoleDetailsResponse> toDetailsResponseList(Iterable<RoleEntity> entities);
}
