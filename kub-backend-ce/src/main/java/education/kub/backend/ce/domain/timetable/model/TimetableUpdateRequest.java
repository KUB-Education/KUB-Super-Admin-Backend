package education.kub.backend.ce.domain.timetable.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record TimetableUpdateRequest(
        @NotNull(message = "Timetable name can't be null")
        @NotBlank(message = "Timetable name can't be blank")
        @Size(min = 1, max = 64, message = "Timetable name must have length in interval [1,64]")
        String name,
        @NotNull(message = "Timetable name can't be null")
        Instant timeStart,
        @NotNull(message = "Timetable name can't be null")
        Instant timeEnd
) {}
