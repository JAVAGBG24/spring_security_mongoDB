package se.java.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.java.security.models.Product;
import se.java.security.services.FilterProductService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test/filter")
public class FilterProductController {
    private final FilterProductService filterProductService;

    @Autowired
    public FilterProductController(FilterProductService filterProductService) {
        this.filterProductService = filterProductService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> filterProducts(@RequestParam Map<String, String> params) {
        List<Product> filteredProducts = filterProductService.filterProducts(params);
        return ResponseEntity.ok(filteredProducts);
    }











}
