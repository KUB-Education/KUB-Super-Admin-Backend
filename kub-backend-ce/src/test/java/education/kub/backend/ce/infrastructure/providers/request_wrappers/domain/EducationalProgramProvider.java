package education.kub.backend.ce.infrastructure.providers.request_wrappers.domain;

import education.kub.backend.ce.infrastructure.properties.auth.LoginProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ExecutorProperties;
import education.kub.backend.ce.infrastructure.properties.executor.RequestProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ResponseValidationProperties;
import education.kub.backend.ce.infrastructure.properties.study_field.EducationalProgramRequestUrlParameters;
import education.kub.backend.ce.infrastructure.providers.executors.RequestExecutor;
import io.restassured.http.Method;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

import static education.kub.backend.ce.infrastructure.providers.request_wrappers.auth.AuthProvider.BearerTokenToMap;

public class EducationalProgramProvider {

    public static String GetEducationalPrograms(ExecutorProperties executor, LoginProperties loginData, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/educational-programs")
                .method(Method.GET)
                .headers(BearerTokenToMap(loginData.accessToken)).build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/study_field/__EducationalProgramDetailsResponseArray.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String GetEducationalProgramById(ExecutorProperties executor, LoginProperties loginData,
                                     EducationalProgramRequestUrlParameters params, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/educational-programs/" + params.toUrlRequestParametersSubstring())
                .method(Method.GET)
                .headers(BearerTokenToMap(loginData.accessToken)).build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/study_field/EducationalProgramDetailsResponse.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String UpdateEducationalProgramById(ExecutorProperties executor, LoginProperties loginData,
                                        Map<String, Object> request_body, EducationalProgramRequestUrlParameters params,
                                        HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/educational-programs/" + params.toUrlRequestParametersSubstring())
                .method(Method.PUT)
                .body(request_body)
                .headers(BearerTokenToMap(loginData.accessToken)).build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/study_field/EducationalProgramDetailsResponse.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String DeleteEducationalProgramById(ExecutorProperties executor, LoginProperties loginData,
                                        EducationalProgramRequestUrlParameters params, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/educational-programs/" + params.toUrlRequestParametersSubstring())
                .method(Method.DELETE)
                .headers(BearerTokenToMap(loginData.accessToken)).build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }
}
