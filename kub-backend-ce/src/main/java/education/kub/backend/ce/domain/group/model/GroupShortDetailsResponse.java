package education.kub.backend.ce.domain.group.model;

import java.time.Instant;

public record GroupShortDetailsResponse(
        Long id,

        String name,

        Instant createdAt
) {
}
