package education.kub.superadmin.controller.handler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.zalando.problem.Problem;
import org.zalando.problem.StatusType;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import java.net.URI;

@ConditionalOnClass({ProblemHandling.class})
public class BaseExceptionHandling {
    protected Problem buildProblem(String uri, StatusType status, Throwable exception) {
        return Problem.builder()
                .withInstance(URI.create(uri))
                .withTitle(status.getReasonPhrase())
                .withStatus(status)
                .withDetail(exception.getMessage())
                .build();
    }
}
