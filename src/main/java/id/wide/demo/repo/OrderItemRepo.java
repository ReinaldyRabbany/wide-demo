package id.wide.demo.repo;

import id.wide.demo.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
    Set<OrderItem> findAllByOrderId(UUID orderId);
}
