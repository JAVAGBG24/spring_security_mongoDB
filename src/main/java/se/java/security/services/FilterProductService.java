package se.java.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.java.security.models.Product;
import se.java.security.repository.ProductRepository;

@Service
public class FilterProductService {
    private final ProductRepository<Product> productRepository;

    @Autowired
    public FilterProductService(ProductRepository<Product> productRepository) {
        this.productRepository = productRepository;
    }
}
