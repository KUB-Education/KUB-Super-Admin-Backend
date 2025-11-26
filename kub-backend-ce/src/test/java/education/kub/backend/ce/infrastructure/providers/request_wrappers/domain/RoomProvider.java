package education.kub.backend.ce.infrastructure.providers.request_wrappers.domain;

import education.kub.backend.ce.infrastructure.properties.auth.LoginProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ExecutorProperties;
import education.kub.backend.ce.infrastructure.properties.executor.RequestProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ResponseValidationProperties;
import education.kub.backend.ce.infrastructure.properties.room.RoomRequestUrlParameters;
import education.kub.backend.ce.infrastructure.providers.executors.RequestExecutor;
import io.restassured.http.Method;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

import static education.kub.backend.ce.infrastructure.providers.request_wrappers.auth.AuthProvider.BearerTokenToMap;

public class RoomProvider {

    public static String CreateRoom(ExecutorProperties executor, LoginProperties loginData,
                                    Map<String, Object> request_body, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/rooms")
                .method(Method.POST)
                .headers(BearerTokenToMap(loginData.accessToken))
                .body(request_body).build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/room/RoomDetailsResponse.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String GetRooms(ExecutorProperties executor, LoginProperties loginData, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/rooms")
                .method(Method.GET)
                .headers(BearerTokenToMap(loginData.accessToken)).build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/room/__RoomDetailsResponseArray.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String GetRoomById(ExecutorProperties executor, LoginProperties loginData,
                                     RoomRequestUrlParameters params, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/rooms/" + params.room_id)
                .method(Method.GET)
                .headers(BearerTokenToMap(loginData.accessToken)).build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/room/RoomDetailsResponse.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String UpdateRoomById(ExecutorProperties executor, LoginProperties loginData,
                                        Map<String, Object> request_body, RoomRequestUrlParameters params,
                                        HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/rooms/" + params.room_id)
                .method(Method.PUT)
                .body(request_body)
                .headers(BearerTokenToMap(loginData.accessToken)).build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .ValidationSchema("schemas/room/RoomDetailsResponse.json")
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }

    public static String DeleteRoomById(ExecutorProperties executor, LoginProperties loginData,
                                        RoomRequestUrlParameters params, HttpStatusCode expectedStatusCode) {
        RequestProperties request_params = RequestProperties.builder()
                .url(executor.conn.base_url + "/api/v1/rooms/" + params.room_id)
                .method(Method.DELETE)
                .headers(BearerTokenToMap(loginData.accessToken)).build();

        ResponseValidationProperties validation = ResponseValidationProperties.builder()
                .StatusCode(expectedStatusCode)
                .build();

        return RequestExecutor.ExecuteRequest(executor.mvc, request_params, validation);
    }
}