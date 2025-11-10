package education.kub.backend.ce.infrastructure.providers.mocks.WebMvc;

import education.kub.backend.ce.app.exception.handler.GlobalExceptionHandler;
import education.kub.backend.ce.app.filter.JwtAuthFilter;
import education.kub.backend.ce.infrastructure.providers.factories.JacksonMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Component
public class MockMvcProvider {
    private static GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    void set_handler(@Autowired GlobalExceptionHandler handler) {
        globalExceptionHandler = handler;
    }

    public static MockMvc createAndSetupMockMvc(JwtAuthFilter filter,  Object... controllers) {
        return MockMvcBuilders.standaloneSetup(controllers)
                .addFilter(filter)
                .addFilter(new UsernamePasswordAuthenticationFilter())
                .setControllerAdvice(globalExceptionHandler)
                .setMessageConverters(JacksonMapperFactory.createJacksonMapper())
                .build();
    }
}
