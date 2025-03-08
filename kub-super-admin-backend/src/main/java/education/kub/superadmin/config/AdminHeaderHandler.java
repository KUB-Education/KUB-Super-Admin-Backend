package education.kub.superadmin.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminHeaderHandler implements HandlerInterceptor {

    @Value("${super.admin.key}")
    private String superAdminSecret;
    private static final String ADMIN_KEY_HEADER = "X-SUPER-ADMIN-KEY";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String headerValue = request.getHeader(ADMIN_KEY_HEADER);

        if (headerValue == null || !headerValue.equals(superAdminSecret)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Missing or invalid X-SUPER-ADMIN-KEY");
            return false;
        }
        return true;
    }
}
