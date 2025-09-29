package education.kub.backend.ce.domain.user.model;

public record UserCreateRequest(
        String lastName,

        String firstName,

        String middleName,

        String email
) {
}
