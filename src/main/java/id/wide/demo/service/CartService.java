package id.wide.demo.service;

import id.wide.demo.dto.request.AddProductCartRequest;
import id.wide.demo.dto.response.CartResponse;
import id.wide.demo.dto.CustomerDTO;
import id.wide.demo.dto.ProductDTO;
import id.wide.demo.entity.*;
import id.wide.demo.exception.ProductAvailabilityException;
import id.wide.demo.repo.ProductCartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final ProductCartRepo productCartRepo;

    private final CustomerService customerService;
    private final ProductService productService;

    @Autowired
    public CartService(ProductCartRepo productCartRepo, CustomerService customerService,
                       ProductService productService) {
        this.productCartRepo = productCartRepo;
        this.productService = productService;
        this.customerService = customerService;
    }

    @Transactional(readOnly = true)
    public CartResponse getCart(long customerId) {
        Customer customer = customerService.getCustomer(customerId);
        Set<ProductCart> productCarts = productCartRepo.findByCustomerId(customerId);

        CartResponse response = new CartResponse();

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setAddress(customer.getAddress());
        response.setCustomer(customerDTO);

        Set<ProductDTO> products = productCarts.stream().map(p -> {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(p.getProductId());
            Product product = productService.getProduct(p.getProductId());
            productDTO.setName(product.getName());
            productDTO.setType(product.getType());
            productDTO.setPrice(product.getPrice());
            productDTO.setQuantity(p.getQuantity());
            return productDTO;
        }).collect(Collectors.toSet());

        response.setProducts(products);
        return response;
    }

    @Transactional(readOnly = true)
    private Optional<ProductCart> getProductCart(Long customerId, Long productId) {
        return productCartRepo.findProductCart(customerId, productId);
    }

    @Transactional
    public void addProductToCart(AddProductCartRequest request) throws ProductAvailabilityException {
        if (Boolean.FALSE.equals(customerService.isCustomerExists(request.getCustomerId()))
                || Boolean.FALSE.equals(productService.isProductExists(request.getProductId()))) {
            throw new NoSuchElementException();
        }
        if (Boolean.FALSE.equals(productService.checkProductAvailability(
                request.getProductId(), request.getQuantity()))) {
            throw new ProductAvailabilityException();
        }

        ProductCart productCart = getProductCart(request.getCustomerId(),request.getProductId()).orElse(new ProductCart());
        if (productCart.getQuantity()==null) {
            productCart.setCustomerId(request.getCustomerId());
            productCart.setProductId(request.getProductId());
            productCart.setQuantity(request.getQuantity());
        } else
            productCart.setQuantity(productCart.getQuantity() + request.getQuantity());
        productCartRepo.save(productCart);
    }
}
