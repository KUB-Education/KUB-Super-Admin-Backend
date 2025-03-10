package education.kub.superadmin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import education.kub.superadmin.entities.UserEntity;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AdminResponseDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("middle_name")
    private String middleName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("status")
    private UserEntity.Status status;
}
