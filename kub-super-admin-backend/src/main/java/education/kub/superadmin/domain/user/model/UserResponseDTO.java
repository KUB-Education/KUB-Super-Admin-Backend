package education.kub.superadmin.domain.user.model;

import education.kub.superadmin.domain.user.entity.UserEntity;

public record UserResponseDTO(
        Long id,

        String lastName,

        String firstName,

        String middleName,

        String email,

        UserEntity.Status status
) {}
