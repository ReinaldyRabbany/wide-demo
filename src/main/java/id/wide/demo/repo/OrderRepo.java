package id.wide.demo.repo;

import id.wide.demo.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<OrderDetail, UUID> {
    Set<OrderDetail> findAllByCustomerId(Long customerId);
}
