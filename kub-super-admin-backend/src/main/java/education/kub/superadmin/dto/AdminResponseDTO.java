package education.kub.superadmin.dto;

import education.kub.superadmin.entities.UserEntity;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AdminResponseDTO {
    private Long id;
    private Long userId;
    private String lastName;
    private String firstName;
    private String middleName;
    private String email;
    private UserEntity.Status status;
}
