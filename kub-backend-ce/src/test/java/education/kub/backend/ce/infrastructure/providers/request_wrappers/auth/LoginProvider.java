package education.kub.backend.ce.infrastructure.providers.request_wrappers.auth;

import education.kub.backend.ce.infrastructure.properties.executor.ExecutorProperties;
import education.kub.backend.ce.infrastructure.properties.auth.LoginProperties;
import education.kub.backend.ce.infrastructure.properties.executor.RequestProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ResponseValidationProperties;
import io.restassured.path.json.JsonPath;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static education.kub.backend.ce.infrastructure.providers.request_wrappers.auth.AuthProvider.BearerTokenToMap;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Component
@TestPropertySource(locations = {"classpath:test.application.properties"})
public class LoginProvider {

    static String Login(ExecutorProperties executor, Map<String, Object> request_map,
                        String bearer_token, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .body(request_map)
                .headers(BearerTokenToMap(bearer_token))
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/auth/LoginResponse.json")
                .build();

        return AuthProvider.Login(executor, request_params, validation);
    }

    public static void ValidateLogin(ExecutorProperties executor, Map<String, Object> request_map,
                                     String bearer_token, HttpStatusCode expectedStatusCode) {
        Login(executor, request_map, bearer_token, expectedStatusCode);
    }

    public static void FirstLogin(ExecutorProperties executor, LoginProperties loginData) {

        String response = Login(executor, Map.of("email", loginData.user.email, "password", loginData.user.password),
                null, HttpStatusCode.valueOf(200));

        JsonPath jsonPath = JsonPath.with(response);
        boolean is_first_login = jsonPath.get("first_login");
        assertFalse(is_first_login, "First login field must be set to false at not first login attempt");

        loginData.accessToken = jsonPath.get("access_token");
        loginData.refreshToken = jsonPath.get("refresh_token");
    }
}
