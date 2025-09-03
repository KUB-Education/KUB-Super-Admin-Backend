package education.kub.backend.ce.domain.user.model;

public record UserUpdateRequest(
        String lastName,

        String firstName,

        String middleName,

        String email
) {
}
