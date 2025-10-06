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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthProvider {
    public static String Login(MockMvc mvc, String uri, Map<String, Object> request_body, String bearer_token,
                               int expectedStatusCode, String JsonValidationSchema) {
        try {
            var request = MockMvcRequestBuilders
                            .post(uri + "/api/v1/auth/login")
                            .contentType("application/json")
                            .accept(APPLICATION_JSON);
            if (bearer_token != null) {
                HttpHeaders headers = new HttpHeaders();
                ArrayList<String> list = new ArrayList<String>();
                list.add("Bearer " + bearer_token);
                headers.put("Authorization", list);
                request.headers(headers);
            }

            if (request_body != null) {
                JSONObject jsonObject = new JSONObject(request_body);
                var JsonBody = jsonObject.toString();
                request.content(JsonBody);
            }
            ResultActions result = mvc.perform(request).andDo(print()).andExpect(status().is(expectedStatusCode));
            if (expectedStatusCode == 200) {
                result.andExpect(content().string(matchesJsonSchemaInClasspath(JsonValidationSchema)));
            }
            return result.andReturn().getResponse().getContentAsString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void Logout(MockMvc mvc, String uri, String access_token, int expectedStatusCode, boolean autorization) {
        try {
            var request = MockMvcRequestBuilders
                                    .post(uri+"/api/v1/auth/logout")
                                    .contentType("application/json")
                                    .accept(APPLICATION_JSON);
            if (autorization) {
                HttpHeaders headers = new HttpHeaders();
                ArrayList<String> list = new ArrayList<String>();
                list.add("Bearer " + ((access_token != null)? access_token: ""));
                headers.put("Authorization", list);
                request.headers(headers);
            }
           mvc.perform(request).andDo(print()).andExpect(status().is(expectedStatusCode));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String Refresh(MockMvc mvc, String uri, Map<String, Object> request_body,
                                 int expectedStatusCode, String JsonValidationSchema) {
        String JsonBody = "";
        if  (request_body != null) {
            JSONObject jsonObject = new JSONObject(request_body);
            JsonBody = jsonObject.toString();
        }

        try {
            ResultActions result = mvc.perform(MockMvcRequestBuilders
                                        .post(uri + "/api/v1/auth/refresh")
                                        .contentType("application/json")
                                        .content(JsonBody)
                                        .accept(APPLICATION_JSON))
                                        .andDo(print())
                                        .andExpect(status().is(expectedStatusCode));
            if (expectedStatusCode == 200) {
                result.andExpect(content().string(matchesJsonSchemaInClasspath(JsonValidationSchema)));
            }
            return result.andReturn().getResponse().toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}