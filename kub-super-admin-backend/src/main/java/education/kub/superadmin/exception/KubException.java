package education.kub.superadmin.exception;

import lombok.Getter;

@Getter
public class KubException extends RuntimeException {
    private ErrorCode errorCode;
    public KubException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
