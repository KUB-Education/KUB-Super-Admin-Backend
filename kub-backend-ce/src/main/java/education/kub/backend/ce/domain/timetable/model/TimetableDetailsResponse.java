package education.kub.backend.ce.domain.timetable.model;


import education.kub.backend.ce.domain.timetable.entity.TimetableEntity;

import java.time.Instant;

public record TimetableDetailsResponse(
        Long id,

        String name,

        Instant timeStart,

        Instant timeEnd,

        Long group,

        TimetableEntity.Status status
) { }
