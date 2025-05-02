package se.java.security.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.java.security.models.Category;
import se.java.security.models.Product;
import se.java.security.models.Sizes;
import se.java.security.services.ProductService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<List<Product>> getProductsByColor(@PathVariable String color) {
        List<Product> products = productService.getProductsByColor(color);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Category category) {
        List<Product> products = productService.getProductsByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @GetMapping("/size/{size}/in-stock")
    public ResponseEntity<List<Product>> getProductsBySizeInStock(@PathVariable Sizes size) {
        List<Product> products = productService.getProductsBySizeInStock(size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/color/{color}/size/{size}/in-stock")
    public ResponseEntity<List<Product>> getProductsByColorAndSizeInStock(
            @PathVariable String color,
            @PathVariable Sizes size) {
        List<Product> products = productService.getProductsByColorAndSizeInStock(color, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/category/{category}/size/{size}/in-stock")
    public ResponseEntity<List<Product>> getProductsByCategoryAndSizeInStock(
            @PathVariable Category category,
            @PathVariable Sizes size) {
        List<Product> products = productService.getProductsByCategoryAndSizeInStock(category, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/price-range/size/{size}/in-stock")
    public ResponseEntity<List<Product>> getProductsByPriceRangeAndSizeInStock(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @PathVariable Sizes size) {
        List<Product> products = productService.getProductsByPriceRangeAndSizeInStock(minPrice, maxPrice, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @PatchMapping("/{id}/inventory")
    public ResponseEntity<Product> updateInventory(
            @PathVariable String id,
            @RequestBody Map<Sizes, Integer> inventoryUpdates) {
        Product updatedProduct = productService.updateInventory(id, inventoryUpdates);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PatchMapping("/{id}/inventory/{size}")
    public ResponseEntity<Product> updateInventoryForSize(
            @PathVariable String id,
            @PathVariable Sizes size,
            @RequestParam int quantity) {
        Product updatedProduct = productService.updateInventoryForSize(id, size, quantity);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }


    // PUT
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    // PATCH
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> patchProduct(@PathVariable String id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.patchProduct(id, product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
