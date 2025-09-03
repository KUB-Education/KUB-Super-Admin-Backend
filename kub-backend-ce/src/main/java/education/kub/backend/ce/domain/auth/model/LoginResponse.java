package education.kub.backend.ce.domain.auth.model;

public record LoginResponse(
        String accessToken,

        String refreshToken,

        Boolean firstLogin
) {}