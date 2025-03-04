package education.kub.superadmin.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNAUTHORIZED("Unauthorized (Wrong header)", HttpStatus.UNAUTHORIZED),
    CONFLICT("User exists", HttpStatus.CONFLICT),
    NOT_FOUNT("Admin not found", HttpStatus.NOT_FOUND),
    SMTP_FAILURE("SMTP failure", HttpStatus.SERVICE_UNAVAILABLE);

    final HttpStatus status;
    final String message;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
