package education.kub.backend.ce.app.exception.model;

import lombok.Getter;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

@Getter
public class KubException extends BaseException {
    public KubException(ErrorCode errorCode) {
        super(errorCode.status, errorCode.message);
    }

    @Getter
    public enum ErrorCode {
        UNAUTHORIZED("Unauthorized (Wrong header)", Status.UNAUTHORIZED),
        CONFLICT("Object already exists", Status.CONFLICT),
        NOT_FOUND("Object not found", Status.NOT_FOUND),
        SMTP_FAILURE("SMTP failure", Status.SERVICE_UNAVAILABLE);

        private final StatusType status;
        private final String message;

        ErrorCode(String message, StatusType status) {
            this.message = message;
            this.status = status;
        }
    }
}
