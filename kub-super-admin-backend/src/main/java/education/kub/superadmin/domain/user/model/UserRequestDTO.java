package education.kub.superadmin.domain.user.model;

public record UserRequestDTO(
        String lastName,

        String firstName,

        String middleName,

        String email
) {
}
