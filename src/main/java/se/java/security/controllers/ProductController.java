package se.java.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.java.security.models.Product;
import se.java.security.services.ProductService;

import java.util.List;
import java.util.Optional;

/**
 * BASIC CRUD REST CONTROLLER
 * *
 * PURPOSE: This controller provides standard REST endpoints for basic CRUD operations on products.
 * It follows RESTful conventions and delegates all business logic to ProductService.
 * *
 * REST ENDPOINT MAPPING:
 * - GET /api/product -> Get all products
 * - GET /api/product/{id} -> Get product by ID
 * - POST /api/product -> Create new product
 * - PUT /api/product/{id} -> Update existing product
 * - DELETE /api/product/{id} -> Delete product
 * *
 * CORS CONFIGURATION:
 * - @CrossOrigin allows requests from any origin (*) for development/testing
 * - maxAge = 3600 caches preflight requests for 1 hour
 * *
 * GENERIC USAGE:
 * - Uses ProductService<Product> to work with any Product subclass
 * - JSON serialization/deserialization handled automatically by Jackson using the @JsonTypeInfo annotations
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService<Product> productService;

    @Autowired
    public ProductController(ProductService<Product> productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable String id) {
        Optional<Product> product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product product) {
        Product updateProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updateProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}