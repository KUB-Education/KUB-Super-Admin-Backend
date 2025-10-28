package education.kub.backend.ce.domain.role.model;

import education.kub.backend.ce.domain.role.entity.RoleEntity;

public record RoleDetailsResponse(
        Long id,

        RoleEntity.Type type
) {
}
