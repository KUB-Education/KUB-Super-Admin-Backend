package education.kub.superadmin.app.interceptor;

import education.kub.superadmin.app.properties.AppSecurityProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class SuperAdminHeaderInterceptor implements HandlerInterceptor {

    private static final String ADMIN_KEY_HEADER = "X-SUPER-ADMIN-KEY";

    private final AppSecurityProperties appSecurityProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String headerValue = request.getHeader(ADMIN_KEY_HEADER);

        if (headerValue == null || !headerValue.equals(appSecurityProperties.superAdminHeaderSecret())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Missing or invalid " + ADMIN_KEY_HEADER);

            return false;
        }

        return true;
    }
}
