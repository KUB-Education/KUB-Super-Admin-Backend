package education.kub.backend.ce.domain.group.model;

import education.kub.backend.ce.domain.student.model.StudentShortDetailsResponse;

import java.time.Instant;
import java.util.List;

public record GroupFullDetailsResponse(
        Long id,

        String name,

        Instant createdAt,

        List<StudentShortDetailsResponse> students
) {
}
