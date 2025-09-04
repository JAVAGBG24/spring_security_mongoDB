package se.java.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.java.security.services.FilterProductService;

@RestController
@RequestMapping("/api/products/filter")
public class FilterProductController {
    private final FilterProductService filterProductService;

    @Autowired
    public FilterProductController(FilterProductService filterProductService) {
        this.filterProductService = filterProductService;
    }
}
