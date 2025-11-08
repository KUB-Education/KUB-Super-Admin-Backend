package education.kub.backend.ce.domain.timetable.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record TimetableCreateRequest(
        @Pattern(regexp = "^(?!\\s*$).+",
                message = "Name must contain at least one non-whitespace character")
        @Size(min = 1, max = 64, message = "Timetable name must have length in interval [1,64]")
        String name,
        @NotBlank(message = "Timetable start time can't be blank")
        Instant timeStart,
        @NotBlank(message = "Timetable end time can't be blank")
        Instant timeEnd
) {}
