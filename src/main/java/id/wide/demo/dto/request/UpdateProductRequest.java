package id.wide.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateProductRequest {
    @NotNull
    private Long id;
    @NotBlank @Size(max = 100)
    private String name;
    @NotBlank @Size(max = 100)
    private String type;
    @NotNull @Min(0)
    private BigDecimal price;
    @NotNull @Min(0)
    private Long quantity;
}
