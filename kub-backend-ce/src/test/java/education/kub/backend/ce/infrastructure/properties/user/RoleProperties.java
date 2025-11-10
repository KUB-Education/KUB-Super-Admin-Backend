package education.kub.backend.ce.infrastructure.properties.user;

import education.kub.backend.ce.domain.role.entity.RoleEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class RoleProperties {
    public final long id = 0;

    public final RoleEntity.Type role = RoleEntity.Type.USER;
}
