package education.kub.superadmin.domain.admin.model;

import education.kub.superadmin.domain.user.entity.UserEntity;

public record AdminResponseDTO(
        Long id,

        String lastName,

        String firstName,

        String middleName,

        String email,

        UserEntity.Status userStatus
) {}
