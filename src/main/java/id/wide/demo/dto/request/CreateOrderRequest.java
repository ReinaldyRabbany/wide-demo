package id.wide.demo.dto.request;

import id.wide.demo.dto.OrderItemDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CreateOrderRequest {
    @NotNull
    private Long customerId;
    @NotBlank @Size(max = 100)
    private String customerName;
    @NotBlank @Size(max = 100)
    private String customerAddress;
    @NotNull @Min(0)
    private BigDecimal total;
    @Valid @NotNull
    private Set<OrderItemDTO> items;
}
