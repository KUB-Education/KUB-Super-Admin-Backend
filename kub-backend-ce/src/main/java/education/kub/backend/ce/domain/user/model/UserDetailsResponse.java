package education.kub.backend.ce.domain.user.model;

import education.kub.backend.ce.domain.role.model.RoleDetailsResponse;
import education.kub.backend.ce.domain.user.entity.UserEntity;

import java.util.List;

public record UserDetailsResponse(
        Long id,

        String lastName,

        String firstName,

        String middleName,

        String email,

        UserEntity.Status status,

        List<RoleDetailsResponse> roles
) {
}
