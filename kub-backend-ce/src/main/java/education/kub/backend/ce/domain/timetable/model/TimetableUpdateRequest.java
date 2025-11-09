package education.kub.backend.ce.domain.timetable.model;

import education.kub.backend.ce.domain.timetable.entity.TimetableEntity;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record TimetableUpdateRequest(
        @Pattern(regexp = "^(?!\\s*$).+",
                message = "Name must contain at least one non-whitespace character")
        @Size(min = 1, max = 64, message = "Timetable name must have length in interval [1,64]")
        String name,

        Instant timeStart,

        Instant timeEnd,

        Long groupId,

        TimetableEntity.Status status
) {}
