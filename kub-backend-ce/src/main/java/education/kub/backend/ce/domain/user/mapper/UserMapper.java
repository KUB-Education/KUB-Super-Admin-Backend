package education.kub.backend.ce.domain.user.mapper;

import education.kub.backend.ce.domain.role.mapper.RoleMapper;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.model.UserDetailsResponse;
import education.kub.backend.ce.domain.user.model.UserRegistrationData;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
    UserDetailsResponse toDetailsResponse(UserEntity entity);

    List<UserDetailsResponse> toDetailsResponseList(Iterable<UserEntity> entities);

    UserRegistrationData toRegistrationData(UserEntity entity);
}
