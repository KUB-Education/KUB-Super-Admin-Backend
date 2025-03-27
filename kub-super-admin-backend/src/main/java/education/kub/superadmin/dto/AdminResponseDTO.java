package education.kub.superadmin.dto;

import education.kub.superadmin.entities.UserEntity;

public record AdminResponseDTO(
        Long id,

        Long userId,

        String lastName,

        String firstName,

        String middleName,

        String email,

        UserEntity.Status status
) {}
