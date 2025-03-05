package education.kub.superadmin.dto;

import education.kub.superadmin.entities.UserEntity;

public class AdminResponseDTO {
    private Long id;
    private Long userId;
    private String lastName;
    private String firstName;
    private String middleName;
    private String email;
    private UserEntity.Status status;
}
