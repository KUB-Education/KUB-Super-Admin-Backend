package education.kub.backend.ce.domain.auth.controller;

import education.kub.backend.ce.helpers.requests.RequestExecutor;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;


public class AuthProvider extends RequestExecutor {

    public static Map<String, String> BearerTokenToMap(String bearerToken) {
        return (bearerToken != null) ? Map.of("Authorization", "Bearer " + bearerToken): null;
    }

    public static Map<String, String> RefreshTokenToMap(String refreshToken) {
        return (refreshToken != null) ? Map.of("refresh_token", refreshToken): null;
    }

    public static String Login(MockMvc mvc, String uri, Map<String, String> request_body, String bearer_token,
                               HttpStatusCode expectedStatusCode, String JsonValidationSchema) {
        return CommonRequest(mvc, uri + "/api/v1/auth/login", request_body, BearerTokenToMap(bearer_token),
                expectedStatusCode, JsonValidationSchema);
    }

    public static void Logout(MockMvc mvc, String uri, String access_token, HttpStatusCode expectedStatusCode, boolean authorization) {
        String full_uri = uri + "/api/v1/auth/logout";
        if (authorization) {
            CommonRequest(mvc, full_uri, null, BearerTokenToMap(access_token), expectedStatusCode, null);
        }
        else {
            CommonRequest(mvc, full_uri, null, null, expectedStatusCode, null);
        }
    }

    public static String Refresh(MockMvc mvc, String uri, Map<String, String> request_body,
                                 HttpStatusCode expectedStatusCode, String JsonValidationSchema) {
        return CommonRequest(mvc, uri + "/api/v1/auth/refresh", request_body, null, expectedStatusCode,  JsonValidationSchema);
    }
}