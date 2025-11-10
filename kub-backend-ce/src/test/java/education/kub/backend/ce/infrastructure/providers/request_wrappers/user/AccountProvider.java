package education.kub.backend.ce.infrastructure.providers.request_wrappers.user;

import education.kub.backend.ce.infrastructure.properties.auth.LoginProperties;
import education.kub.backend.ce.infrastructure.properties.executor.RequestProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ResponseValidationProperties;
import education.kub.backend.ce.infrastructure.providers.executors.RequestExecutor;

import static education.kub.backend.ce.infrastructure.providers.request_wrappers.auth.AuthProvider.BearerTokenToMap;

import io.restassured.http.Method;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class AccountProvider  {

    public static String GetUserAccountInfo(LoginProperties loginData, HttpStatusCode expectedStatusCode) {

        RequestProperties request_params = RequestProperties.builder()
                .url(loginData.executor.conn.base_url + "/api/v1/account/me")
                .method(Method.GET)
                .headers(BearerTokenToMap(loginData.accessToken))
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .build();

        return RequestExecutor.ExecuteRequest(loginData.executor.mvc, request_params, validation);
    }

    public static void ChangePassword(LoginProperties loginData, Map<String,String> request_body, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(loginData.executor.conn.base_url + "/api/v1/account/change-password")
                .body(request_body)
                .headers(BearerTokenToMap(loginData.accessToken))
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .build();

        RequestExecutor.ExecuteRequest(loginData.executor.mvc, request_params, validation);
    }

    public static void RecoverPassword(LoginProperties loginData, Map<String,String> request_body,
                                       HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(loginData.executor.conn.base_url + "/api/v1/account/recovery-password")
                .body(request_body)
                .headers(BearerTokenToMap(loginData.accessToken))
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .build();

        RequestExecutor.ExecuteRequest(loginData.executor.mvc, request_params, validation);
    }
}
