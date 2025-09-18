package se.java.security.repository;


import org.springframework.data.mongodb.core.MongoTemplate;
import se.java.security.models.Product;

import java.util.List;
import java.util.Map;

public class CustomProductRepositoryImpl implements CustomProductRepository {

    private final MongoTemplate mongoTemplate;

    public CustomProductRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<Product> findProductByCriteria(Map<String, String> filters, Class<? extends Product> productclass) {
        return List.of();
    }
}