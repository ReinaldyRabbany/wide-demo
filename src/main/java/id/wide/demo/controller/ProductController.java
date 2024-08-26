package id.wide.demo.controller;

import id.wide.demo.dto.request.CreateProductRequest;
import id.wide.demo.dto.response.RestResponse;
import id.wide.demo.dto.request.UpdateProductRequest;
import id.wide.demo.exception.ProductAvailabilityException;
import id.wide.demo.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable long productId) throws ProductAvailabilityException {
        return ResponseEntity.ok(RestResponse.success(productService.getProduct(productId)));
    }

    @GetMapping()
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "1", required = false) @Min(1) Integer page,
                                            @RequestParam(defaultValue = "5", required = false) @Min(1) Integer size) {
        return ResponseEntity.ok(RestResponse.success(productService.getProductsPageOf(page-1,size)));
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody CreateProductRequest request) {
        productService.createProduct(request);
        return ResponseEntity.ok(RestResponse.success());
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@Valid @RequestBody UpdateProductRequest request) throws ProductAvailabilityException {
        productService.updateProduct(request);
        return ResponseEntity.ok(RestResponse.success());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> hardDeleteProduct(@PathVariable long productId) throws ProductAvailabilityException {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(RestResponse.success());
    }
}
