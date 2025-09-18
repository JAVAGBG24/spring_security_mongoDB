package se.java.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.java.security.models.*;
import se.java.security.repository.ProductRepository;

import java.util.List;
import java.util.Map;

@Service
public class FilterProductService {

    private final ProductRepository productRepository;

    @Autowired
    public FilterProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // FILTER METOD
    public List<Product> filterProducts(Map<String, String> params) {
        // STEG 1: affärslogiken för produkttyp
        String productTypeAlias = getProductType(params.get("productType"));

        // STEG 2: konvertering till data access representation
        Class<? extends Product> productClass = getProductClassFromAlias(productTypeAlias);

        // STEG 3: delegera data till access layer för den faktiska filtreringen
        return productRepository.findProductByCriteria(params, productClass);
    }

    private String getProductType(String typeName) {
        if (typeName == null || typeName.isEmpty()) {
            return Product.class.getSimpleName();
        }

        switch(typeName.toLowerCase()) {
            case "collar":
                return Collar.class.getSimpleName();
            case "leash":
                return Leash.class.getSimpleName();
            case "toy":
                return Toy.class.getSimpleName();
            case "bowl":
                return Bowl.class.getSimpleName();
            default:
                throw new IllegalArgumentException("Unknown product type: " + typeName);
        }
    }

    private Class<? extends Product> getProductClassFromAlias(String alias) {
        switch (alias) {
            case "Collar":
                return Collar.class;
            case "Leash":
                return Leash.class;
            case "Toy":
                return Toy.class;
            case "Bowl":
                return Bowl.class;
            default:
                // DEFENSIVE DEFAULT: Om mapping misslyckas, sök bland alla produkter
                return Product.class;
        }
    }
}



