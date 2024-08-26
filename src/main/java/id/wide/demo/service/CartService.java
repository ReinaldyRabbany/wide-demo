package id.wide.demo.service;

import id.wide.demo.dto.CustomerDTO;
import id.wide.demo.dto.ProductCartDTO;
import id.wide.demo.dto.request.AddProductCartRequest;
import id.wide.demo.dto.response.CartResponse;
import id.wide.demo.entity.Customer;
import id.wide.demo.entity.Product;
import id.wide.demo.entity.ProductCart;
import id.wide.demo.exception.ProductAvailabilityException;
import id.wide.demo.repo.ProductCartRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
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

        BigDecimal total = BigDecimal.ZERO;
        Set<ProductCartDTO> products = new HashSet<>();
        for (ProductCart productCart: productCarts) {
            try {
                Product product = productService.getProduct(productCart.getProductId());
                ProductCartDTO productCartDTO = new ProductCartDTO();
                productCartDTO.setId(productCart.getProductId());
                productCartDTO.setName(product.getName());
                productCartDTO.setType(product.getType());
                productCartDTO.setPrice(product.getPrice());
                productCartDTO.setQuantity(productCart.getQuantity());
                productCartDTO.setTotal(product.getPrice().multiply(BigDecimal.valueOf(productCart.getQuantity())));
                products.add(productCartDTO);

                total = total.add(productCartDTO.getTotal());
            } catch (Exception e) {
                log.error("can't fetch product " + productCart.getProductId());
            }
        }
        response.setProducts(products);
        response.setTotal(total);
        return response;
    }

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

    @Transactional
    public void removeProductOnCart(Long customerId, Set<Long> productIds) {
        Set<ProductCart> productCarts = new HashSet<>();
        for (Long productId : productIds) {
            Optional<ProductCart> productCartOpt = getProductCart(customerId, productId);
            productCartOpt.ifPresent(productCarts::add);
        }
        productCartRepo.deleteAll(productCarts);
    }
}
