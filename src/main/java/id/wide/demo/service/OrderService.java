package id.wide.demo.service;

import id.wide.demo.dto.OrderItemDTO;
import id.wide.demo.dto.request.CreateOrderRequest;
import id.wide.demo.entity.OrderDetail;
import id.wide.demo.entity.OrderItem;
import id.wide.demo.entity.Product;
import id.wide.demo.exception.ProductAvailabilityException;
import id.wide.demo.repo.OrderItemRepo;
import id.wide.demo.repo.OrderRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;

    private final CustomerService customerService;
    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public OrderService(OrderRepo orderRepo, OrderItemRepo orderItemRepo,
                        CustomerService customerService, ProductService productService, CartService cartService) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.customerService = customerService;
        this.productService = productService;
        this.cartService = cartService;
    }

    @Transactional(readOnly = true)
    public OrderDetail getOrderDetail(String orderId) throws NoSuchElementException {
        try {
            return orderRepo.findById(UUID.fromString(orderId)).orElseThrow();
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException();
        }
    }

    @Transactional(readOnly = true)
    public Set<OrderDetail> getOrderByCustomer(Long customerId) {
        Set<OrderDetail> orders = orderRepo.findAllByCustomerId(customerId);
        orders.forEach(order -> {
            order.setItems(orderItemRepo.findAllByOrderId(order.getId()));
        });
        return orders;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(CreateOrderRequest request) throws ProductAvailabilityException {
        if (Boolean.FALSE.equals(customerService.isCustomerExists(request.getCustomerId()))) {
            throw new EntityNotFoundException("Customer Entity Not Found");
        }

        OrderDetail order = new OrderDetail();
        order.setCustomerId(request.getCustomerId());
        order.setCustomerName(request.getCustomerName());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setTotal(request.getTotal());
        order.setCreatedDate(new Date());

        Set<Long> productIds = new HashSet<>();
        for (OrderItemDTO itemDTO: request.getItems()) {
            OrderItem item = new OrderItem();
            Product product = productService.getProduct(itemDTO.getProductId());

            if (Boolean.FALSE.equals(productService.checkProductAvailability(product, itemDTO.getQuantity()))) {
                throw new ProductAvailabilityException();
            }

            item.setProductId(itemDTO.getProductId());
            item.setName(itemDTO.getName());
            item.setType(itemDTO.getType());
            item.setPrice(itemDTO.getPrice());
            item.setQuantity(itemDTO.getQuantity());
            item.setOrder(order);
            order.addItem(item);

            product.setQuantity(product.getQuantity() - itemDTO.getQuantity());
            productIds.add(itemDTO.getProductId());
        }

        orderItemRepo.saveAll(order.getItems());
        orderRepo.save(order);
        cartService.removeProductOnCart(request.getCustomerId(), productIds);
    }
}
