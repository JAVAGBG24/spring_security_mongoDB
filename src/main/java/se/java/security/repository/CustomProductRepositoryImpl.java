package se.java.security.repository;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import se.java.security.models.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class CustomProductRepositoryImpl implements CustomProductRepository {

    private final MongoTemplate mongoTemplate;

    public CustomProductRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private static final Map<Class<? extends Product>, String> PRODUCT_TYPE_ALIAS_MAP = Map.of(
            Collar.class, "collarProduct",
            Leash.class, "leashProduct",
            Toy.class, "toyProduct",
            Bowl.class, "bowlProduct"
    );

    private String getProductTypeAlias(Class<? extends Product> productClass) {
        return PRODUCT_TYPE_ALIAS_MAP.getOrDefault(productClass, "product");
    }


    /**
     * Metodöversikt:
     * 1. Skapa tom Query och Criteria objekt
     * 2. Hantera special fall såsom min-maxPrice intervall
     * 3. Filters för vanliga produktattribut såsom color etc
     * 4. Applicera alla filter från input-map
     * 5. Lägg till produktfilter baserat på @TypeAlias
     * 6. Köra Query och returnera resultat
     * *
     * CRITERIA BYGGS UPP STEG FÖR STEG MED METHOD CHAINING
     */
    @Override
    public List<Product> findProductByCriteria(Map<String, String> filters, Class<? extends Product> productclass) {
       // STEG 1:  initiera query building object
        Query query = new Query();
        Criteria criteria = new Criteria();

       // STEG 2: Specialfall
        if(filters.containsKey("minPrice") || filters.containsKey("maxPrice")) {
            double minPrice = filters.containsKey("minPrice") ?
                    Double.parseDouble(filters.get("minPrice")) : Double.MIN_VALUE;
            double maxPrice = filters.containsKey("maxPrice") ?
                    Double.parseDouble(filters.get("maxPrice")) : Double.MAX_VALUE;

            // range query
            // gte = greater than or equal
            // lte = less than or equal
            criteria.and("price").gte(minPrice).lte(maxPrice);
        }

       // STEG 3: Filter handlers (vanliga attribut)

       // istället för vi ska ha ett stort block av if/else kommer vi använda en Map av BiConsumer funktioner
        // BiConsumer<Criteria, String> = en funktion som tar Criteria och filter värde som input
        Map<String, BiConsumer<Criteria, String>> filterHandlers = new HashMap<>();

        // gemensamma för alla produkttyper
        // name, color
        // regex option "i" = case INsensitive
        filterHandlers.put("name", (crit, value) -> crit.and("name").regex(value, "i"));
        filterHandlers.put("color", (crit, value) -> crit.and("color").is(value));

        // STEG 4: specifika filters för varje produkt
        if(Collar.class.equals(productclass)) {
            filterHandlers.put("size", (crit, value) -> crit.and("size").is(value));
        }

        if(Leash.class.equals(productclass) || Collar.class.equals(productclass)) {
            filterHandlers.put("material", (crit, value) -> crit.and("material").is(value));
        }

        // STEG 5: lägg till alla filters
        // lambda = ->
        // defensiv programmering: om filtret inte finns (här ovanför) så skiter vi bara i det
        // istället för systemet ska krascha
        filters.forEach((key, value) -> {
            if(filterHandlers.containsKey(key)) {
                filterHandlers.get(key).accept(criteria, value);
            }
            // okända filter ignoreras hade dock vart bra om man kunde logga ut dom för debug
        });

        // STEG 6: produkttyp filtrering
        String productTypeAlias = getProductTypeAlias(productclass);
        criteria.and("_class").is(productTypeAlias);

        // STEG 7: köra query
        // mongoTemplate.find kommer returnera en List<T> där T är den produkten vi specificerar
        return mongoTemplate.find(query, (Class<Product>) productclass);
    }
}























