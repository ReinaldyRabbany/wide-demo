package id.wide.demo.service;

import id.wide.demo.dto.request.CreateProductRequest;
import id.wide.demo.dto.request.UpdateProductRequest;
import id.wide.demo.entity.Product;
import id.wide.demo.exception.ProductAvailabilityException;
import id.wide.demo.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Boolean isProductExists(long productId) {
        return productRepo.existsById(productId);
    }

    @Transactional(readOnly = true)
    public Product getProduct(long productId) throws ProductAvailabilityException {
        return productRepo.findById(productId).orElseThrow(ProductAvailabilityException::new);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsPageOf(int page, int size) {
        return productRepo.findAll(PageRequest.of(page,size));
    }

    @Transactional
    public void createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setType(request.getType());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        productRepo.save(product);
    }

    @Transactional
    public void updateProduct(UpdateProductRequest request) throws ProductAvailabilityException {
        Product product = getProduct(request.getId());
        product.setName(request.getName());
        product.setType(request.getType());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        productRepo.save(product);
    }

    @Transactional
    public void deleteProduct(long id) throws ProductAvailabilityException {
        productRepo.delete(getProduct(id));
    }

    @Transactional(readOnly = true)
    public Boolean checkProductAvailability(long productId, long quantity) {
        return productRepo.checkQuantity(productId, quantity);
    }

    public Boolean checkProductAvailability(Product product, long quantity) {
        return product.getQuantity() >= quantity;
    }
}