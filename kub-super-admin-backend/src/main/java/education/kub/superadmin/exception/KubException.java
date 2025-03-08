package education.kub.superadmin.exception;

import lombok.Getter;

@Getter
public class KubException extends BaseException {
    private ErrorCode errorCode;
    public KubException(ErrorCode errorCode) {
        super(errorCode.getStatus(), errorCode.getMessage());
    }
}
