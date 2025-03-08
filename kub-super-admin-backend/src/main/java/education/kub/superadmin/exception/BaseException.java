package education.kub.superadmin.exception;

import lombok.Getter;
import org.zalando.problem.StatusType;

@Getter
public class BaseException extends RuntimeException {
    private final transient StatusType status;

    public BaseException(StatusType status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public BaseException(StatusType status, String message) {
        super(message);
        this.status = status;
    }
}
