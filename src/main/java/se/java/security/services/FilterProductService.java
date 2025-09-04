package se.java.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.java.security.filter.FilterRegistry;
import se.java.security.filter.ProductFilter;
import se.java.security.models.*;
import se.java.security.repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class FilterProductService {
    private final ProductRepository<Product> productRepository;

    @Autowired
    public FilterProductService(ProductRepository<Product> productRepository) {
        this.productRepository = productRepository;
    }

    // main filter metod
    public List<Product> filterProducts(Map<String, String> params) {
        // steg 1: konvertera produkt typen till mongodb _class värde
        String productTypeAlias = getProductType(params.get("productType"));

        // steg 2: hämta passande filters för specifik produkt typ från FilterRegistry
        Map<String, ProductFilter<Product>> filters = FilterRegistry.getFiltersForType((getProductClassFromAlias(productTypeAlias)));

        // steg 3: bygger predikat baserat på parametrar
        // konvertera varje matchande parameter till Predicate<Product> och den använder FilterRegistry
        List<Predicate<Product>> predicates = params.entrySet().stream()
                .filter(entry -> filters.containsKey(entry.getKey()))
                .map(entry -> {
                    String paramName = entry.getKey();
                    String paramValue = entry.getValue();
                    return filters.get(paramName).apply(paramValue);
                })
                .collect(Collectors.toList());

        // steg 4: kombinerar alla predikat genom att använda logisk AND operator
        // kommer starta i typ "always true" predikat efter det kommer (AND) alla andra predikat, dessa kommer att bli tillsammans
        // resultat: vi kommer att ha ETT predikat som testa ALLA olika filter kriterier (conditions)
        Predicate<Product> combinedPredicate = predicates.stream()
                .reduce(product -> true, Predicate::and);

        // steg 5: tier 1 filtrering hämta produkter av en specifik produkt typ från databasen
        // detta gör att vi reducerar datan (typ filtrerar ut lite grann) innan vi gör en filtrering på applikations nivå
        List<Product> allProducts = fetchProductsByType(productTypeAlias);

        // steg 6: tier 2 filtrering, vi lägger till combinedPredicate i minnet
        // filtrerar resultatet från databasen genom att använda alla olika filter conditions vi samlat ihop
        return allProducts.stream()
                .filter(combinedPredicate)
                .collect(Collectors.toList());
    }

    // OBS! hjälpmetoder bör inte ligga i servicen här gör dom det för att kod-demon ska bli lättare att följa
    // hjälpmetod konvertera product type till _class alias
    private String getProductType(String typeName) {
        if (typeName == null || typeName.isEmpty()) {
            return "product";
        }

        switch (typeName.toLowerCase()) {
            case "collar":
                return "collarProduct";
            case "leash":
                return "leashProduct";
            case "toy":
                return "toyProduct";
            case "bowl":
                return "bowlProduct";
            default:
                throw new IllegalArgumentException("Unknown product type " + typeName);
        }
    }


    // hjälpmetod Tier 1 databasfiltering, hämta produkt typ från databasen
    private List<Product> fetchProductsByType (String productTypeAlias) {
        return productRepository.findByProductType(productTypeAlias);
    }


    // hjälpmetod konvertera databas alias tillbaka till java klass objektet
    private Class<? extends Product> getProductClassFromAlias(String alias) {
        switch (alias) {
            case "collarProduct":
                return Collar.class;
            case "leashProduct":
                return Leash.class;
            case "toyProduct":
                return Toy.class;
            case "bowlProduct":
                return Bowl.class;
            default:
                return Product.class;
        }
    }
}
