package education.kub.superadmin.app.exception.handler;

import education.kub.superadmin.app.exception.model.ValidationExceptionErrorDTO;
import education.kub.superadmin.app.exception.model.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.autoconfigure.ProblemAutoConfiguration;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@ConditionalOnClass({ProblemHandling.class})
@AutoConfigureBefore({ProblemAutoConfiguration.class})
public class GlobalExceptionHandler extends BaseExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Problem> handleException(BaseException ex, HttpServletRequest request) {
        final Problem problem = buildProblem(request.getRequestURI(), ex.getStatus(), ex);
        return ResponseEntity.status(Objects.requireNonNull(problem.getStatus()).getStatusCode()).body(problem);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Problem> handleException(@NonNull final ResponseStatusException ex, final NativeWebRequest request) {
        final Problem problem = buildProblem(Objects.requireNonNull(request.getNativeRequest(HttpServletRequest.class)).getRequestURI(),
                Status.valueOf(ex.getStatusCode().value()), ex);
        return ResponseEntity.status(Objects.requireNonNull(problem.getStatus()).getStatusCode()).body(problem);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Problem> handleException(HttpClientErrorException ex, HttpServletRequest request) {
        final Problem problem = buildProblem(request.getRequestURI(), Status.valueOf(ex.getStatusCode().value()), ex);
        return ResponseEntity.status(Objects.requireNonNull(problem.getStatus()).getStatusCode()).body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Problem> handleException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        final Problem problem = buildProblem(request.getRequestURI(), Status.BAD_REQUEST, ex);
        return ResponseEntity.status(Objects.requireNonNull(problem.getStatus()).getStatusCode()).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleException(Exception ex, HttpServletRequest request) {
        final Problem problem = buildProblem(request.getRequestURI(), Status.INTERNAL_SERVER_ERROR, ex);
        return ResponseEntity.status(Objects.requireNonNull(problem.getStatus()).getStatusCode()).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.unprocessableEntity().body(new ValidationExceptionErrorDTO(errors));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Problem> handleValidationExceptions(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        final Problem problem = buildProblem(request.getRequestURI(), Status.BAD_REQUEST, ex);
        return ResponseEntity.status(Objects.requireNonNull(problem.getStatus()).getStatusCode()).body(problem);
    }
}
