package education.kub.backend.ce.domain.user.model;

import education.kub.backend.ce.domain.role.entity.RoleEntity;

public record UserRemoveRoleRequest(
        RoleEntity.Type type
) {}
