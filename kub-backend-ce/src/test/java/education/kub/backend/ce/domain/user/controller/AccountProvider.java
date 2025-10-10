package education.kub.backend.ce.domain.user.controller;

import education.kub.backend.ce.helpers.requests.RequestExecutor;

import static education.kub.backend.ce.domain.auth.controller.AuthProvider.BearerTokenToMap;

import io.restassured.http.Method;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

public class AccountProvider extends RequestExecutor {

    public static String GetUserAccountInfo(MockMvc mockMvc, String backend_url, String accessToken, HttpStatusCode expectedStatusCode) {
        return ExecuteRequest(mockMvc, Method.GET, backend_url + "/api/v1/account/me", null,
                null, null, BearerTokenToMap(accessToken),
                        expectedStatusCode, "schemas/UserDetailsResponse.json");
    }

    public static String ChangePassword(MockMvc mockMvc, String backend_url, Map<String,String> request_body, String accessToken,
                               HttpStatusCode expectedStatusCode) {
        return CommonRequest(mockMvc, backend_url + "/api/v1/account/change-password", request_body,
                BearerTokenToMap(accessToken), expectedStatusCode, "schemas/UserChangePasswordResponse.json");
    }

    public static String RecoverPassword(MockMvc mockMvc, String backend_url, Map<String,String> request_body, String accessToken,
                                HttpStatusCode expectedStatusCode) {
        return CommonRequest(mockMvc, backend_url + "/api/v1/account/recovery-password",
                request_body, BearerTokenToMap(accessToken), expectedStatusCode, null);
    }
}
