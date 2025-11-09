package education.kub.backend.ce.domain.timetable.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record TimetableCreateRequest(
        @Pattern(regexp = "^(?!\\s*$).+",
                message = "Name must contain at least one non-whitespace character")
        @Size(min = 1, max = 64, message = "Timetable name must have length in interval [1,64]")
        String name,

        @NotNull(message = "Timetable start time can't be null")
        Instant timeStart,

        @NotNull(message = "Timetable end time can't be null")
        Instant timeEnd,

        @NotNull(message = "Timetable group id can't be null")
        Long groupId
) {}
