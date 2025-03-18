package education.kub.superadmin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminRequestDTO {
    @JsonProperty("last_name")
    @NotBlank(message = "last_name can't be blank")
    @Size(min = 1, max = 32, message = "last_name must have length in interval [1,32]")
    @Pattern(regexp = "^(?=.{1,32}$)[A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]" +
            "+(?:[-'ʼ][A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]+)*$",
            message = "last_name must contain only Latin or Cyrillic letters, hyphen, apostrophe")
    private String lastName;

    @JsonProperty("first_name")
    @NotBlank(message = "first_name can't be blank")
    @Size(min = 1, max = 32, message = "first_name must have length in interval [1,32]")
    @Pattern(regexp = "^(?=.{1,32}$)[A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]" +
            "+(?:[-'ʼ][A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]+)*$",
            message = "first_name must contain only Latin or Cyrillic letters, hyphen, apostrophe")
    private String firstName;

    @JsonProperty("middle_name")
    @Size(min = 1, max = 32, message = "middle_name must have length in interval [1,32]")
    @Pattern(regexp = "^(?=.{1,32}$)[A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]" +
            "+(?:[-'ʼ][A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]+)*$",
            message = "middle_name must contain only Latin or Cyrillic letters, hyphen, apostrophe")
    private String middleName;

    @JsonProperty("email")
    @NotBlank(message = "email can't be blank")
    @Size(max = 64, message = "email must have length <= 64")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "email must be a valid email address")
    private String email;
}
