package id.wide.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RestResponse<T> {
    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public T data;

    public static RestResponse success() {
        return RestResponse.builder()
                .code("200")
                .message("SUCCESS")
                .build();
    }

    public static RestResponse success(Object data) {
        return RestResponse.builder()
                .code("200")
                .message("SUCCESS")
                .data(data)
                .build();
    }
}
