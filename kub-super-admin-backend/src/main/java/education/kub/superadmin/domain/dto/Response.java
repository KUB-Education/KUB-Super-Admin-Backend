package education.kub.superadmin.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response<T> {
    private String message;
    private T data;

    public static <T> Response<T> message(String message) {
        return Response.<T>builder()
                .message(message)
                .build();
    }

    public static <T> Response<T> data(T data) {
        return Response.<T>builder()
                .data(data)
                .build();
    }
}
