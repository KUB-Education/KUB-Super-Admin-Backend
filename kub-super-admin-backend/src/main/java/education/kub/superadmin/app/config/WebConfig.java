package education.kub.superadmin.app.config;

import education.kub.superadmin.app.interceptor.SuperAdminHeaderInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final SuperAdminHeaderInterceptor superAdminHeaderInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(superAdminHeaderInterceptor)
                .addPathPatterns("/admins/**");
    }
}