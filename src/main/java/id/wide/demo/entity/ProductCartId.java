package id.wide.demo.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductCartId implements Serializable {
    private long customerId;
    private long productId;
}
