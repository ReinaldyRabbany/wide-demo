package id.wide.demo.dto.response;

import id.wide.demo.dto.CustomerDTO;
import id.wide.demo.dto.ProductCartDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class CartResponse {
    private CustomerDTO customer;
    private Set<ProductCartDTO> products;
    private BigDecimal total;
}
