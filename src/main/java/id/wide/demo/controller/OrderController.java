package id.wide.demo.controller;

import id.wide.demo.dto.request.CreateOrderRequest;
import id.wide.demo.dto.response.RestResponse;
import id.wide.demo.exception.ProductAvailabilityException;
import id.wide.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId) throws NoSuchElementException {
        return ResponseEntity.ok(RestResponse.success(orderService.getOrderDetail(orderId)));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getOrderByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(RestResponse.success(orderService.getOrderByCustomer(customerId)));
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderRequest request) throws ProductAvailabilityException {
        orderService.createOrder(request);
        return ResponseEntity.ok(RestResponse.success());
    }
}
