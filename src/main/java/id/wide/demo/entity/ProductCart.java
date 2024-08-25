package id.wide.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IdClass(ProductCartId.class)
@Entity(name = "product_cart")
public class ProductCart {
    @Id
    private long customerId;
    @Id
    private long productId;
    private Long quantity;
}
