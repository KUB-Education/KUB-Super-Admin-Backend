package education.kub.backend.ce.infrastructure.providers.executors;

import education.kub.backend.ce.infrastructure.properties.executor.RequestProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ResponseValidationProperties;
import io.restassured.http.Method;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static education.kub.backend.ce.infrastructure.providers.attachments.AttachmentBuilder.AttachRequest;
import static education.kub.backend.ce.infrastructure.providers.attachments.AttachmentBuilder.AttachResponse;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
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

    static MockHttpServletRequestBuilder BuildRequest(RequestProperties request_params)
    {
        MockHttpServletRequestBuilder request = createRequest(request_params.method, request_params.url);

        if (request_params.headers != null) {
            HttpHeaders request_headers = new HttpHeaders();
            for (var entry : request_params.headers.entrySet()) {
                request_headers.add(entry.getKey(), entry.getValue());
            }
            request.headers(request_headers);
        }

        if (request_params.body != null) {
            JSONObject jsonObject = new JSONObject(request_params.body);
            var JsonBody = jsonObject.toString();
            request.content(JsonBody);
        }

        if (request_params.contentType != null) {
            request.contentType(request_params.contentType);
        }

        if (request_params.acceptMediaType != null) {
            request.accept(request_params.acceptMediaType);
        }

        return request;
    }

    static String GetAndValidateResponse(MockMvc mvc, MockHttpServletRequestBuilder request,
                                         ResponseValidationProperties validation) {
        try {
            var perform = mvc.perform(request);

            var result = perform.andReturn();
            var result_request = result.getRequest();
            var result_response = result.getResponse();

            AttachRequest(result_request);
            AttachResponse(result_response);

            if (validation.StatusCode != null) {
                perform.andExpect(status().is(validation.StatusCode.value()));
            }

            if (validation.ValidationSchema != null) {
                perform.andExpect(content().string(matchesJsonSchemaInClasspath(validation.ValidationSchema)));
            }

            return result_response.getContentAsString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static String ExecuteRequestImpl(MockMvc mvc, RequestProperties request_params,
                                 ResponseValidationProperties validation) {
        MockHttpServletRequestBuilder request = BuildRequest(request_params);
        return GetAndValidateResponse(mvc, request, validation);
    }

    public static String ExecuteRequest(MockMvc mvc, RequestProperties request_params,
                                        ResponseValidationProperties validation) {
        if (validation.StatusCode == HttpStatus.OK)  {
            return ExecuteRequestImpl(mvc, request_params, validation);
        }
        validation.ValidationSchema = null;
        return ExecuteRequestImpl(mvc, request_params, validation);
    }
}
