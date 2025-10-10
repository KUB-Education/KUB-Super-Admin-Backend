package education.kub.backend.ce.helpers.requests;

import io.restassured.http.Method;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RequestExecutor {
     static MockHttpServletRequestBuilder createRequest(Method method, String uri) {
        return switch (method) {
            case GET -> MockMvcRequestBuilders.get(uri);
            case POST -> MockMvcRequestBuilders.post(uri);
            case PUT -> MockMvcRequestBuilders.put(uri);
            case DELETE -> MockMvcRequestBuilders.delete(uri);
            case PATCH -> MockMvcRequestBuilders.patch(uri);
            case OPTIONS -> MockMvcRequestBuilders.options(uri);
            case HEAD -> MockMvcRequestBuilders.head(uri);
            default -> throw new IllegalArgumentException("Invalid method type");
        };
    }

    static MockHttpServletRequestBuilder BuildRequest(Method method, String uri,
                                                      String contentType, MediaType acceptMediaType,
                                                      Map<String, String> request_body,
                                                      Map<String, String> headers)
    {
        MockHttpServletRequestBuilder request = createRequest(method, uri);

        if (headers != null) {
            HttpHeaders request_headers = new HttpHeaders();
            for (var entry : headers.entrySet()) {
                request_headers.add(entry.getKey(), entry.getValue());
            }
            request.headers(request_headers);
        }

        if (request_body != null) {
            JSONObject jsonObject = new JSONObject(request_body);
            var JsonBody = jsonObject.toString();
            request.content(JsonBody);
        }

        if (contentType != null) {
            request.contentType(contentType);
        }

        if (acceptMediaType != null) {
            request.accept(acceptMediaType);
        }

        return request;
    }

    static String GetAndValidateResponse(MockMvc mvc, MockHttpServletRequestBuilder request,
                                         HttpStatusCode expectedStatusCode, String JsonValidationSchema) {
        try {
            var result = mvc.perform(request)
                    .andDo(print());

            if (expectedStatusCode != null) {
                result.andExpect(status().is(expectedStatusCode.value()));
            }

            if (JsonValidationSchema != null) {
                result.andExpect(content().string(matchesJsonSchemaInClasspath(JsonValidationSchema)));
            }

            return result.andReturn().getResponse().getContentAsString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

     static String ExecuteRequestImpl(MockMvc mvc, Method method, String uri,
                                        String contentType, MediaType acceptMediaType,
                                        Map<String, String> request_body,
                                        Map<String, String> headers,
                                        HttpStatusCode expectedStatusCode, String JsonValidationSchema) {
        MockHttpServletRequestBuilder request = BuildRequest(method, uri, contentType, acceptMediaType, request_body, headers);
        return GetAndValidateResponse(mvc, request, expectedStatusCode, JsonValidationSchema);
    }

    public static String ExecuteRequest(MockMvc mvc, Method method, String uri,
                                                       String contentType, MediaType acceptMediaType,
                                                       Map<String, String> request_body,
                                                       Map<String, String> headers,
                                                       HttpStatusCode expectedStatusCode, String JsonValidationSchema) {
        if (expectedStatusCode == HttpStatus.OK)  {
            return ExecuteRequestImpl(mvc, method, uri,  contentType, acceptMediaType, request_body,
                    headers, expectedStatusCode, JsonValidationSchema);
        }
        return ExecuteRequestImpl(mvc, method, uri,  contentType, acceptMediaType, request_body,
                headers, expectedStatusCode, null);
    }

    public static String CommonRequest(MockMvc mvc, String uri, Map<String, String> request_body,
                                Map<String, String> headers, HttpStatusCode expectedStatusCode, String JsonValidationSchema) {
        return ExecuteRequest(mvc, Method.POST, uri, "application/json", APPLICATION_JSON,
                request_body, headers, expectedStatusCode, JsonValidationSchema);
    }
}
