package education.kub.backend.ce.domain.auth.controller;

import education.kub.backend.ce.helpers.requests.RequestExecutor;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class AuthProvider {

    public static Map<String, String> BearerTokenToMap(String bearerToken) {
        return (bearerToken != null) ? Map.of("Authorization", "Bearer " + bearerToken): null;
    }

    public static Map<String, String> RefreshTokenToMap(String refreshToken) {
        return (refreshToken != null) ? Map.of("refresh_token", refreshToken): null;
    }

    public static String Login(RequestExecutor executor, String uri, Map<String, String> request_body, String bearer_token,
                               HttpStatusCode expectedStatusCode, String JsonValidationSchema) {

        return executor.CommonRequest(uri + "/api/v1/auth/login", request_body, BearerTokenToMap(bearer_token),
                expectedStatusCode, JsonValidationSchema);
    }

    public static void Logout(RequestExecutor executor, String uri, String access_token, HttpStatusCode expectedStatusCode) {
        executor.CommonRequest(uri + "/api/v1/auth/logout", null, BearerTokenToMap(access_token), expectedStatusCode, null);
    }

    public static void Refresh(RequestExecutor executor, String uri, Map<String, String> request_body,
                                 HttpStatusCode expectedStatusCode, String JsonValidationSchema) {
        executor.CommonRequest(uri + "/api/v1/auth/refresh", request_body, null, expectedStatusCode,  JsonValidationSchema);
    }
}