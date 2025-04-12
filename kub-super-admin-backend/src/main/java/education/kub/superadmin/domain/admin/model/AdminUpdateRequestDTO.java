package education.kub.superadmin.domain.admin.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AdminUpdateRequestDTO(
        @Size(min = 1, max = 32, message = "last_name must have length in interval [1,32]")
        @Pattern(regexp = "^(?=.{1,32}$)[A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]" +
                "+(?:[-'ʼ][A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]+)*$",
                message = "last_name must contain only Latin or Cyrillic letters, hyphen, apostrophe")
        String lastName,

        @Size(min = 1, max = 32, message = "first_name must have length in interval [1,32]")
        @Pattern(regexp = "^(?=.{1,32}$)[A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]" +
                "+(?:[-'ʼ][A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]+)*$",
                message = "first_name must contain only Latin or Cyrillic letters, hyphen, apostrophe")
        String firstName,

        @Size(max = 32, message = "middle_name must have length less than or equal to 32")
        @Pattern(regexp = "^$|^(?=.{0,32}$)[A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]" +
                "+(?:[-'ʼ][A-Za-zАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]+)*$",
                message = "middle_name must contain only Latin or Cyrillic letters, hyphen, apostrophe")
        String middleName,

        @Size(max = 64, message = "email must have length <= 64")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "email must be a valid email address")
        String email
) {}

