package id.wide.demo.repo;

import id.wide.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    @Query("select count(p.id)>0 from product p where p.id = :productId and p.quantity >= :quantity")
    Boolean checkQuantity(long productId, long quantity);
}
