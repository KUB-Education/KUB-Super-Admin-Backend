package education.kub.backend.ce.helpers.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import education.kub.backend.ce.domain.auth.controller.AuthProvider;
import education.kub.backend.ce.helpers.requests.RequestExecutor;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

@TestPropertySource(locations = {"classpath:test.application.properties"})
public class LoginProvider extends RequestExecutor {
    @Value("${server.url}")
    protected String backend_url;

    protected final String email = "allex.nevedrov@example.com";
    protected final String password = "password";

    protected String accessToken;
    protected String refreshToken;

    @Value("${spring.jackson.property-naming-strategy}")
    protected PropertyNamingStrategy namingStrategy;

    String Login(Map<String, String> request_map, String bearer_token, HttpStatusCode expectedStatusCode) {
        return AuthProvider.Login(this, backend_url, request_map, bearer_token,
                expectedStatusCode, "schemas/LoginResponse.json");
    }

    @Step("Login")
    public void ValidateLogin(Map<String, String> request_map, String bearer_token, HttpStatusCode expectedStatusCode) {
        Login(request_map, bearer_token, expectedStatusCode);
    }

    @Step("First login")
    public void FirstLogin() {

        String response = Login(Map.of("email", email, "password", password),
                null, HttpStatusCode.valueOf(200));

        JsonPath jsonPath = JsonPath.with(response);
        boolean is_first_login = jsonPath.get("first_login");
        assertFalse(is_first_login, "First login field must be set to false at not first login attempt");

        accessToken = jsonPath.get("access_token");
        refreshToken = jsonPath.get("refresh_token");
    }
}
