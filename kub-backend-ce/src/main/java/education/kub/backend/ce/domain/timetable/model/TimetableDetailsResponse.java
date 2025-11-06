package education.kub.backend.ce.domain.timetable.model;


import education.kub.backend.ce.domain.group.domain.GroupEntity;
import education.kub.backend.ce.domain.timetable.entity.TimetableEntity;

import java.time.Instant;

public record TimetableDetailsResponse(
        Long id,

        String name,

        Instant timeStart,

        Instant timeEnd,

        GroupEntity group,

        TimetableEntity.Status status
) { }
