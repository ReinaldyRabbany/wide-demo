package id.wide.demo.dto.response;

import id.wide.demo.dto.CustomerDTO;
import id.wide.demo.dto.ProductDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CartResponse {
    private CustomerDTO customer;
    private Set<ProductDTO> products;
}
