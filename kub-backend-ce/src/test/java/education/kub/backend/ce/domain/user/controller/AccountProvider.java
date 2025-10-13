package education.kub.backend.ce.domain.user.controller;

import education.kub.backend.ce.helpers.requests.RequestExecutor;

import static education.kub.backend.ce.domain.auth.controller.AuthProvider.BearerTokenToMap;

import io.restassured.http.Method;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class AccountProvider  {

    public static String GetUserAccountInfo(RequestExecutor executor, String backend_url,
                                            String accessToken, HttpStatusCode expectedStatusCode) {
        return executor.ExecuteRequest(Method.GET, backend_url + "/api/v1/account/me", null,
                null, null, BearerTokenToMap(accessToken),
                        expectedStatusCode, "schemas/UserDetailsResponse.json");
    }

    public static void ChangePassword(RequestExecutor executor, String backend_url, Map<String,String> request_body, String accessToken,
                               HttpStatusCode expectedStatusCode) {
        executor.CommonRequest(backend_url + "/api/v1/account/change-password", request_body,
                BearerTokenToMap(accessToken), expectedStatusCode, "schemas/UserChangePasswordResponse.json");
    }

    public static void RecoverPassword(RequestExecutor executor, String backend_url, Map<String,String> request_body, String accessToken,
                                HttpStatusCode expectedStatusCode) {
        executor.CommonRequest(backend_url + "/api/v1/account/recovery-password",
                request_body, BearerTokenToMap(accessToken), expectedStatusCode, null);
    }
}
