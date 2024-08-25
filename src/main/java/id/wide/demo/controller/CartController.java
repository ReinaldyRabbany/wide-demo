package id.wide.demo.controller;

import id.wide.demo.dto.request.AddProductCartRequest;
import id.wide.demo.dto.response.RestResponse;
import id.wide.demo.exception.ProductAvailabilityException;
import id.wide.demo.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCart(@PathVariable long customerId) {
        return ResponseEntity.ok(RestResponse.success(cartService.getCart(customerId)));
    }

    @PostMapping("/add-product")
    public ResponseEntity<?> addProductToCart(@Valid @RequestBody AddProductCartRequest request) throws ProductAvailabilityException {
        cartService.addProductToCart(request);
        return ResponseEntity.ok(RestResponse.success());
    }
}
