package education.kub.backend.ce.domain.auth.model;

public record LoginRequest(
        String email,

        String password
) {
}
