package education.kub.backend.ce.infrastructure.providers.request_wrappers.user;

import education.kub.backend.ce.infrastructure.properties.auth.LoginProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ExecutorProperties;
import education.kub.backend.ce.infrastructure.properties.executor.RequestProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ResponseValidationProperties;
import education.kub.backend.ce.infrastructure.properties.user.UserRequestUrlParameters;
import education.kub.backend.ce.infrastructure.providers.executors.RequestExecutor;
import io.restassured.http.Method;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

import static education.kub.backend.ce.infrastructure.providers.request_wrappers.auth.AuthProvider.BearerTokenToMap;

public class UserProvider {
    public static String CreateUser(ExecutorProperties executor, LoginProperties loginData,
                                    Map<String, String> request_body, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/users")
                .headers(BearerTokenToMap(loginData.accessToken))
                .body(request_body)
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/user/UserDetailsResponse.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String GetUsers(ExecutorProperties executor, LoginProperties loginData, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/users")
                .method(Method.GET)
                .headers(BearerTokenToMap(loginData.accessToken))
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/user/__UserDetailsResponseArray.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String GetUser(ExecutorProperties executor, LoginProperties loginData, UserRequestUrlParameters urlParams,
                                 HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/users/" + urlParams.user_id)
                .method(Method.GET)
                .headers(BearerTokenToMap(loginData.accessToken))
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/user/UserDetailsResponse.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String UpdateUser(ExecutorProperties executor, LoginProperties loginData, Map<String, String> request_body,
                                    UserRequestUrlParameters urlParams,
                                    HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/users/" + urlParams.user_id)
                .method(Method.PUT)
                .headers(BearerTokenToMap(loginData.accessToken))
                .body(request_body)
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/user/UserDetailsResponse.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String DeleteUser(ExecutorProperties executor, LoginProperties loginData,
                                    UserRequestUrlParameters urlParams, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/users/" + urlParams.user_id)
                .method(Method.DELETE)
                .headers(BearerTokenToMap(loginData.accessToken))
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String ResendPassword(ExecutorProperties executor, LoginProperties loginData,
                                        UserRequestUrlParameters urlParams,
                                        HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/users/" + urlParams.user_id + "/resend")
                .method(Method.POST)
                .headers(BearerTokenToMap(loginData.accessToken))
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/user/UserDetailsResponse.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String GetUsersRoles(ExecutorProperties executor, LoginProperties loginData,
                                       HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/roles")
                .method(Method.GET)
                .headers(BearerTokenToMap(loginData.accessToken))
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/user/__UserRoleArray.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String AddUserRole(ExecutorProperties executor, LoginProperties loginData,
                                     UserRequestUrlParameters urlParams, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/users/" + urlParams.user_id + "/roles/" + urlParams.role_id)
                .method(Method.PUT)
                .headers(BearerTokenToMap(loginData.accessToken))
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/user/UserDetailsResponse.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String RemoveUserRole(ExecutorProperties executor, LoginProperties loginData,
                                        UserRequestUrlParameters urlParams, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/users/" + urlParams.user_id + "/roles/" + urlParams.role_id)
                .method(Method.DELETE)
                .headers(BearerTokenToMap(loginData.accessToken))
                .build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/user/UserDetailsResponse.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }
}
