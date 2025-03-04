package education.kub.superadmin.controller.handler;

import education.kub.superadmin.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.autoconfigure.ProblemAutoConfiguration;

import java.util.Objects;

@RestControllerAdvice
@ConditionalOnClass({ProblemHandling.class})
@AutoConfigureBefore({ProblemAutoConfiguration.class})
public class GlobalExceptionHandler extends BaseExceptionHandling {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleException(BaseException ex, HttpServletRequest request) {
        final Problem problem = buildProblem(request.getRequestURI(), ex.getStatus(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Problem> handleException(@NonNull final ResponseStatusException ex, final NativeWebRequest request) {
        final Problem problem = buildProblem(Objects.requireNonNull(request.getNativeRequest(HttpServletRequest.class)).getRequestURI(),
                Status.valueOf(ex.getStatusCode().value()), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Problem> handleException(HttpClientErrorException ex, HttpServletRequest request) {
        final Problem problem = buildProblem(request.getRequestURI(), Status.valueOf(ex.getStatusCode().value()), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleException(Exception ex, HttpServletRequest request) {
        final Problem problem = buildProblem(request.getRequestURI(), Status.INTERNAL_SERVER_ERROR, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }
}
