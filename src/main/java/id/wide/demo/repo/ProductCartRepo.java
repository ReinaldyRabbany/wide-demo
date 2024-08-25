package id.wide.demo.repo;

import id.wide.demo.entity.ProductCart;
import id.wide.demo.entity.ProductCartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductCartRepo extends JpaRepository<ProductCart, ProductCartId> {
    Set<ProductCart> findByCustomerId(Long customerId);

    @Query("select pc from product_cart pc where pc.customerId=:customerId and pc.productId=:productId")
    Optional<ProductCart> findProductCart(Long customerId, Long productId);
}
