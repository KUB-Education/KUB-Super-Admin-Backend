package education.kub.backend.ce.domain.auth.model;

public record RefreshResponse(
        String accessToken,

        String refreshToken
) {
}
