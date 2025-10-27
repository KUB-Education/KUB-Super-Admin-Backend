package education.kub.backend.ce.domain.lecturer.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LecturerCreateRequest(
        @NotBlank(message = "last_name can't be blank")
        @Size(min = 1, max = 32, message = "last_name must have length in interval [1,32]")
        @Pattern(regexp = "^(?=.{1,32}$)[A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]" +
                "+(?:[-'ʼ][A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]+)*$",
                message = "last_name must contain only Latin or Cyrillic letters, hyphen, apostrophe")
        String lastName,

        @NotBlank(message = "first_name can't be blank")
        @Size(min = 1, max = 32, message = "first_name must have length in interval [1,32]")
        @Pattern(regexp = "^(?=.{1,32}$)[A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]" +
                "+(?:[-'ʼ][A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]+)*$",
                message = "first_name must contain only Latin or Cyrillic letters, hyphen, apostrophe")
        String firstName,

        @Size(max = 32, message = "middle_name must have length less than or equal to 32")
        @Pattern(regexp = "^$|^(?=.{1,32}$)[A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]" +
                "+(?:[-'ʼ][A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]+)*$",
                message = "middle_name must contain only Latin or Cyrillic letters, hyphen, apostrophe")
        String middleName,

        @NotBlank(message = "email can't be blank")
        @Size(max = 64, message = "email must have length <= 64")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "email must be a valid email address")
        String email
) {
}
