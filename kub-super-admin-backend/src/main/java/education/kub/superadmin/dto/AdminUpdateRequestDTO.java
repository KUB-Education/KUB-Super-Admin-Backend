package education.kub.superadmin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminUpdateRequestDTO {
    @JsonProperty("last_name")
    @Size(min = 1, max = 32, message = "lastName must have length in interval [1,32]")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "lastName must contain only Latin or Cyrillic letters")
    private String lastName;

    @JsonProperty("first_name")
    @Size(min = 1, max = 32, message = "firstName must have length in interval [1,32]")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "firstName must contain only Latin or Cyrillic letters")
    private String firstName;

    @JsonProperty("middle_name")
    @Size(min = 1, max = 32, message = "middleName must have length in interval [1,32]")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "middleName must contain only Latin or Cyrillic letters")
    private String middleName;

    @JsonProperty("email")
    @Size(max = 64, message = "email must have length <= 64")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "email must be a valid email address")
    private String email;
}
