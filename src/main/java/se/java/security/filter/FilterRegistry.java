package se.java.security.filter;

import se.java.security.models.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// syfte: centralisera alla tillgängliga filter mekaniker som finns för varje produkt
public class FilterRegistry {

    private static final Map<Class<? extends Product>, Map<String, ProductFilter<Product>>> FILTERS = new HashMap<>();

    static {
        Map<String, ProductFilter<Product>> commonFilters = new HashMap<>();
        commonFilters.put("name", value -> product ->
                product.getName().toLowerCase().contains(value.toLowerCase()));
        commonFilters.put("color", value -> product ->
                product.getColor().equalsIgnoreCase(value));
        commonFilters.put("minPrice", value -> {
            double minPrice = Double.parseDouble(value);
            return product -> product.getPrice() >= minPrice;
        });
        commonFilters.put("maxPrice", value -> {
            double maxPrice = Double.parseDouble(value);
            return product -> product.getPrice() <= maxPrice;
        });

        FILTERS.put(Product.class, commonFilters);

        // Filters for Collar
        Map<String, ProductFilter<Product>> collarFilters = new HashMap<>(commonFilters);
        collarFilters.put("size", value -> product -> {
            if (product instanceof Collar) {
                // Subclass-Specific Filters:
                // Inom predikaten utför vi en instans av kontroll för att säkerställa att vi
                // arbeta på rätt underklass före gjutning.
                Collar collar = (Collar) product;
                return collar.getSize().equalsIgnoreCase(value);
            }
            return false;
        });
        collarFilters.put("material", value -> product -> {
            if (product instanceof Collar) {
                Collar collar = (Collar) product;
                return collar.getMaterial().equalsIgnoreCase(value);
            }
            return false;
        });

        FILTERS.put(Collar.class, collarFilters);

        // Filters for Leash
        Map<String, ProductFilter<Product>> leashFilters = new HashMap<>(commonFilters);
        leashFilters.put("length", value -> product -> {
            if (product instanceof Leash) {
                Leash leash = (Leash) product;
                return leash.getLength() == Double.parseDouble(value);
            }
            return false;
        });
        leashFilters.put("material", value -> product -> {
            if (product instanceof Leash) {
                Leash leash = (Leash) product;
                return leash.getMaterial().equalsIgnoreCase(value);
            }
            return false;
        });

        FILTERS.put(Leash.class, leashFilters);

        // Filters for Toy
        Map<String, ProductFilter<Product>> toyFilters = new HashMap<>(commonFilters);
        toyFilters.put("type", value -> product -> {
            if (product instanceof Toy) {
                Toy toy = (Toy) product;
                return toy.getType().equalsIgnoreCase(value);
            }
            return false;
        });

        FILTERS.put(Toy.class, toyFilters);

        // Filters for Bowl
        Map<String, ProductFilter<Product>> bowlFilters = new HashMap<>(commonFilters);
        bowlFilters.put("capacity", value -> product -> {
            if (product instanceof Bowl) {
                Bowl bowl = (Bowl) product;
                return bowl.getCapacity() == Double.parseDouble(value);
            }
            return false;
        });

        FILTERS.put(Bowl.class, bowlFilters);


    }

    // Getter for filters
    public static Map<String, ProductFilter<Product>> getFiltersForType(Class<? extends Product> type) {
        return FILTERS.getOrDefault(type, Collections.emptyMap());
    }
}
