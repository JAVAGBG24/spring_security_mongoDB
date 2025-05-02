package se.java.security.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import se.java.security.exceptions.ResourceNotFoundException;
import se.java.security.models.Category;
import se.java.security.models.Product;
import se.java.security.models.Sizes;
import se.java.security.repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        return productRepository.save(product);
    }


    @Cacheable(value = "products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    @Cacheable(value = "productById")
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }


    public List<Product> getProductsByColor(String color) {
        return productRepository.findByColor(color);
    }

    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    // Size-specific inventory methods
    public List<Product> getProductsBySizeInStock(Sizes size) {
        return productRepository.findBySizeInStock(size);
    }

    public List<Product> getProductsByColorAndSizeInStock(String color, Sizes size) {
        return productRepository.findByColorAndSizeInStock(color, size);
    }

    public List<Product> getProductsByCategoryAndSizeInStock(Category category, Sizes size) {
        return productRepository.findByCategoryAndSizeInStock(category, size);
    }

    public List<Product> getProductsByPriceRangeAndSizeInStock(Double minPrice, Double maxPrice, Sizes size) {
        return productRepository.findByPriceRangeAndSizeInStock(minPrice, maxPrice, size);
    }

    @CacheEvict(value = {"products", "productById"}, allEntries = true)
    public Product updateInventory(String productId, Map<Sizes, Integer> inventoryUpdates) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        if (product.getInventory() == null) {
            product.setInventory(inventoryUpdates);
        } else {
            product.getInventory().putAll(inventoryUpdates);
        }

        return productRepository.save(product);
    }

    @CacheEvict(value = {"products", "productById"}, allEntries = true)
    public Product updateInventoryForSize(String productId, Sizes size, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        product.updateInventory(size, quantity);
        return productRepository.save(product);
    }




    // PUT
    @CacheEvict(value = {"products", "productById"}, allEntries = true)
    public Product updateProduct(String id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());

        return productRepository.save(existingProduct);
    }



    // PATCH
    @CacheEvict(value = {"products", "productById"}, allEntries = true)
    public Product patchProduct(String id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        // uppdatera endast icke null fält
        if (product.getName() != null) {
            existingProduct.setName(product.getName());
        }

        if (product.getPrice() != null) {
            existingProduct.setPrice(product.getPrice());
        }

        if (product.getDescription() != null) {
            existingProduct.setDescription(product.getDescription());
        }
        return productRepository.save(existingProduct);
    }




    @CacheEvict(value = {"products", "productById"}, allEntries = true)
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.delete(product);
    }
}
