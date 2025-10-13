package education.kub.backend.ce.helpers.attachments;

import io.qameta.allure.Attachment;
import j2html.tags.specialized.TrTag;
import org.assertj.core.util.Lists;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static j2html.TagCreator.*;

@Component
public class AttachmentBuilder {
    private static String style_string;

    @Autowired
    void set_style(@Value("classpath:css/allure-report-table.css") Resource css_file) throws IOException {
        style_string = StreamUtils.copyToString(css_file.getInputStream(), StandardCharsets.UTF_8);
    }

    static TrTag BuildHeadersRow(Collection<String> header_names, Function<String, String> headers) {
        List<String> names = Lists.newArrayList(header_names);
        return  tr(
                    td("Headers"),
                    td(
                        table(
                            tbody(
                                each(names, header_name->
                                        tr(
                                                td(header_name),
                                                td(headers.apply(header_name))
                                        )
                                )
                            )
                        ).attr("border", "1").attr("width", "100%")
                    )
                );
    }

    static TrTag BuildBodyRowFromString(String body) {
        try {
            JSONObject json = new JSONObject(body);
            body = json.toString(4);
            return tr(
                    td("Body"),
                    td(body)
            );
        }
        catch (Exception e) {
            return tr(
                    td("Body"),
                    td("Error occurred while parsing json body: " + e.getMessage())
            );
        }
    }

    static TrTag BuildBodyRow(byte[] bodyBytes) {
        if (bodyBytes == null) {
            return tr(
                    td("Body"),
                    td("null")
            );
        }
        return BuildBodyRowFromString(new String(bodyBytes, StandardCharsets.UTF_8));
    }

    static TrTag BuildUriRow(String uri) {
        return tr(td("Uri"), td((uri != null) ? uri : "null"));
    }

    static TrTag BuildMethodRow(String method) {
        return tr(td("Method"), td((method != null) ? method : "null"));
    }

    static TrTag BuildStatusCodeRow(int status_code) {
        return tr(td("Status Code"), td(String.valueOf(status_code)));
    }

    static String BuildAttachment(TrTag ... tags) {
        return html(
                head(
                        title("Title"),
                        style(style_string)
                ),
                body(
                        table(
                                tbody(join(tags))
                        ).attr("border", "1").attr("width", "100%")
                )
        ).render();
    }

    @Attachment
    static public String AttachRequest(MockHttpServletRequest request) throws Exception {
        var uri = BuildUriRow(String.format("%s:%s%s", request.getLocalAddr(), request.getLocalPort(), request.getRequestURI()));
        var method = BuildMethodRow(request.getMethod());
        var headers = BuildHeadersRow(Lists.newArrayList(request.getHeaderNames().asIterator()), request::getHeader);
        var body = BuildBodyRow(request.getContentAsByteArray());
        var a = BuildAttachment(uri, method, headers, body);
        return BuildAttachment(uri, method, headers, body);
    }

    @Attachment
    static public String AttachResponse(MockHttpServletResponse response) throws Exception {
        var status_code = BuildStatusCodeRow(response.getStatus());
        var headers = BuildHeadersRow(response.getHeaderNames(), response::getHeader);
        var body = BuildBodyRow(response.getContentAsByteArray());
        return BuildAttachment(status_code, headers, body);
    }

}
