package id.wide.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductCartRequest {
    @NotNull
    private Long customerId;
    @NotNull
    private Long productId;
    @NotNull
    private Long quantity;
}
