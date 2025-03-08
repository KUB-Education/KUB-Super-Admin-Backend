package education.kub.superadmin.exception;

import lombok.Getter;
import org.zalando.problem.Status;

@Getter
public enum ErrorCode {
    UNAUTHORIZED("Unauthorized (Wrong header)", Status.UNAUTHORIZED),
    CONFLICT("User exists", Status.CONFLICT),
    NOT_FOUNT("Admin not found", Status.NOT_FOUND),
    SMTP_FAILURE("SMTP failure", Status.SERVICE_UNAVAILABLE);

    final Status status;
    final String message;

    ErrorCode(String message, Status status) {
        this.message = message;
        this.status = status;
    }
}
