package education.kub.backend.ce.infrastructure.providers.request_wrappers.auth;

import education.kub.backend.ce.infrastructure.properties.executor.ExecutorProperties;
import education.kub.backend.ce.infrastructure.properties.auth.LoginProperties;
import education.kub.backend.ce.infrastructure.properties.executor.RequestProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ResponseValidationProperties;
import education.kub.backend.ce.infrastructure.providers.executors.RequestExecutor;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class AuthProvider {

    public static Map<String, String> BearerTokenToMap(String bearerToken) {
        return (bearerToken != null) ? Map.of("Authorization", "Bearer " + bearerToken): null;
    }

    public static Map<String, String> RefreshTokenToMap(String refreshToken) {
        return (refreshToken != null) ? Map.of("refresh_token", refreshToken): null;
    }

    public static String Login(ExecutorProperties executor, RequestProperties request_params,
                               ResponseValidationProperties validation) {
        request_params.url = executor.conn.base_url + "/api/v1/auth/login";

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static void Logout(LoginProperties loginData, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(loginData.executor.conn.base_url + "/api/v1/auth/logout")
                .headers(AuthProvider.BearerTokenToMap(loginData.accessToken))
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .build();

        RequestExecutor.ExecuteRequest(loginData.executor.mvc, request_params, validation);
    }

    public static void Refresh(ExecutorProperties executor, Map<String, String> request_body,
                               HttpStatusCode expectedStatusCode, String JsonValidationSchema) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/auth/refresh")
                .body(request_body)
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema(JsonValidationSchema)
                .build();

        RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }
}