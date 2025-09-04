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

    /**
     * MAIN FILTERING METHOD - Implements the complete two-tier filtering process.
     * PROCESS FLOW:
     * 1. Extract and convert product type from parameters
     * 2. Get appropriate filters for that product type from FilterRegistry
     * 3. Build a combined predicate from all applicable filter parameters
     * 4. Fetch products of the specified type from database (TIER 1 filtering)
     * 5. Apply the combined predicate to filter results in memory (TIER 2 filtering)
     *
     * @param params Map of filter parameters (e.g., {"productType": "collar", "size": "M", "minPrice": "10"})
     * @return List of products matching all filter criteria
     */
    public List<Product> filterProducts(Map<String, String> params) {
        // STEP 1: Convert user-friendly product type to MongoDB _class value
        // Example: "collar" becomes "collarProduct" for database query
        String productTypeAlias = getProductType(params.get("productType"));

        // STEP 2: Get the appropriate filters for this product type from FilterRegistry
        // Example: For Collar.class, gets common filters plus size/material filters
        Map<String, ProductFilter<Product>>  filters = FilterRegistry.getFiltersForType((getProductClassFromAlias(productTypeAlias)));

        // STEP 3: Build predicates from parameters
        // Convert each matching parameter into a Predicate<Product> using the FilterRegistry
        List<Predicate<Product>> predicates = params.entrySet().stream()
                .filter(entry -> filters.containsKey(entry.getKey()))
                .map(entry ->{
                    String paramName = entry.getKey();
                    String paramValue = entry.getValue();
                    return filters.get(paramName).apply(paramValue);
                })
                .collect(Collectors.toList());

        // STEP 4: Combine all predicates using logical AND
        // Start with "always true" predicate, then AND all other predicates together
        // Result: A single predicate that tests all filter conditions
        Predicate<Product> combinedPredicate = predicates.stream()
                .reduce(product -> true, Predicate::and);

        // STEP 5: TIER 1 FILTERING - Get products of specific type from database
        // This reduces the dataset before application-level filtering
        List<Product> allProducts = fetchProductsByType(productTypeAlias);

        // STEP 6: TIER 2 FILTERING - Apply combined predicate in memory
        // Filter the database results using all the accumulated filter conditions
        return allProducts.stream()
                .filter(combinedPredicate)
                .collect(Collectors.toList());
    }

    /**
     * HELPER METHOD: Convert user-friendly product type names to MongoDB _class aliases
     * *
     * PURPOSE: The frontend uses simple names like "collar", but MongoDB's _class field
     * contains full class names. This method maps between them.
     * *
     * MAPPING LOGIC:
     * - "collar" -> "collarProduct" (maps to com.example.security.models.Collar)
     * - "leash" -> "leashProduct" (maps to com.example.security.models.Leash)
     * - null/empty -> "product" (maps to base Product class)
     *
     * @param typeName User-provided product type (e.g., "collar")
     * @return Database alias for the product type (e.g., "collarProduct")
     */
    private String getProductType(String typeName) {
        if (typeName == null || typeName.isEmpty()) {
            return "product";
        }
        switch(typeName.toLowerCase()) {
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

    /**
     * HELPER METHOD: TIER 1 DATABASE FILTERING - Fetch products by type from database
     * *
     * PURPOSE: This method executes the database-level filtering using the custom repository query.
     * It leverages the @Query("{ '_class' : ?0 }") method to only retrieve documents of the specified type.
     * *
     * DATABASE OPERATION:
     * - Executes MongoDB query: db.products.find({"_class": "com.example.security.models.Collar"})
     * - Only documents matching the exact class type are returned
     * - This significantly reduces network traffic and memory usage
     *
     * @param productTypeAlias The database alias for the product type
     * @return List of products of the specified type from the database
     */
    private List<Product> fetchProductsByType (String productTypeAlias) {
        return productRepository.findByProductType(productTypeAlias);
    }

    /**
     * HELPER METHOD: Convert database aliases back to Java Class objects
     * *
     * PURPOSE: The FilterRegistry needs actual Class objects to look up the appropriate filters.
     * This method converts the database aliases back to the corresponding Java classes.
     * *
     * REVERSE MAPPING:
     * - "collarProduct" -> Collar.class
     * - "leashProduct" -> Leash.class
     * - etc.
     *
     * @param alias Database alias (e.g., "collarProduct")
     * @return Corresponding Java class for FilterRegistry lookup
     */    private Class<? extends Product> getProductClassFromAlias(String alias) {
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