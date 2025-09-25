package education.kub.backend.ce.domain.user.model;

public record UserPasswordChangeRequest(
        String oldPassword,

        String newPassword
) {
}
