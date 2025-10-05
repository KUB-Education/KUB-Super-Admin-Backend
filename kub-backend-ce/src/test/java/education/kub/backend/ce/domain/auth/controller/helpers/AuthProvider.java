package education.kub.backend.ce.domain.auth.controller.helpers;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest(classes = BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
//        properties = {"server.port=10000"})
public class AuthProvider {
    public static String Login(MockMvc mvc, String uri, Map<String, Object> request_body, String bearer_token,
                                 int expectedStatusCode, String JsonValidationSchema) {
        HttpHeaders headers = new HttpHeaders();
        ArrayList<String> list = new ArrayList<String>();
        list.add("application/json");
        headers.put("Content-Type", list);

        if (bearer_token != null) {
            list.clear();
            list.add("Bearer " + bearer_token);
            headers.put("Authorization", list);
        }
        JSONObject jsonObject = new JSONObject(request_body);
        String JsonBody = jsonObject.toString();

        try {
            ResultActions result = mvc.perform(MockMvcRequestBuilders
                            .post(uri + "/auth/login")
                            .contentType("application/json")
                            .headers(headers)
                            .content(JsonBody)
                            .accept(APPLICATION_JSON))
                            .andExpect(status().is(expectedStatusCode))
                            .andExpect(content().string(matchesJsonSchemaInClasspath(JsonValidationSchema)));
            return result.andReturn().getResponse().toString();
        }
        catch (Exception e) {
                throw new RuntimeException(e);
        }
//        Response response = given()
//                .baseUri(uri)
//                .contentType("application/json")
//                .headers(headers)
//                .body(request_body)
//                .when()
//                .post("/auth/login");
//        response.then()
//                .statusCode(expectedStatusCode)
//                .body(matchesJsonSchemaInClasspath(JsonValidationSchema));
    }

    public static void Logout(MockMvc mvc, String uri, String access_token, int expectedStatusCode) {
        HttpHeaders headers = new HttpHeaders();
        ArrayList<String> list = new ArrayList<String>();
        list.add("Bearer " + access_token);
        headers.put("Authorization", list);

        try {
            mvc.perform(MockMvcRequestBuilders
                            .post(uri+"/auth/logout")
                            .headers(headers))
                    .andExpect(status().is(expectedStatusCode));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
//        given()
//                .baseUri(uri)
//                .header("Authorization", "Bearer " + access_token)
//                .when()
//                .post("/auth/logout")
//                .then()
//                .statusCode(expectedStatusCode);
    }

    public static String Refresh(MockMvc mvc, Map<String, Object> request_body, String uri,
                                   int expectedStatusCode, String JsonValidationSchema) {
        JSONObject jsonObject = new JSONObject(request_body);
        String JsonBody = jsonObject.toString();
        try {
            ResultActions result = mvc.perform(MockMvcRequestBuilders
                                    .post(uri + "/auth/refresh")
                                    .content(JsonBody))
                                    .andExpect(status().is(expectedStatusCode))
                                    .andExpect(content().string(matchesJsonSchemaInClasspath(JsonValidationSchema)));
            return result.andReturn().getResponse().toString();
        }
        catch (Exception e) {
                throw new RuntimeException(e);
            }
//        Response response = given()
//                    .baseUri(uri)
//                    .body(request_body)
//                    .when()
//                    .post("/auth/refresh");
//        response.then()
//                .statusCode(expectedStatusCode)
//                .body(matchesJsonSchemaInClasspath(JsonValidationSchema));
    }
}
