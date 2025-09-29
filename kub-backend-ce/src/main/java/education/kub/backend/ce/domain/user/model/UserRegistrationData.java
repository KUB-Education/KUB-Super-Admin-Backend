package education.kub.backend.ce.domain.user.model;

public record UserRegistrationData(
        String lastName,

        String firstName,

        String middleName,

        String email
) {
}
