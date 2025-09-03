package education.kub.backend.ce.infrastructure.token.model;

import java.util.List;

public record TokenDto(
        Long userId,

        String sessionId,

        List<String> roles
) {
}
