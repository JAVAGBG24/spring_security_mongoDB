package se.java.security.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import se.java.security.models.Product;

import java.util.List;

/**
 * GENERIC REPOSITORY INTERFACE for all Product types
 * *
 * PURPOSE: This interface provides database operations for any Product
 * subclass while maintaining type safety.
 * *
 * GENERIC EXPLANATION:
 * - <T extends Product>: T is a type parameter that must be Product or
 * any subclass of Product
 * - This allows the same repository interface to work with Collar, Leash, Toy, Bowl, etc.
 * - Each implementation can be type-safe for its specific product type
 * *
 * EXTENDS MongoRepository<T, String>:
 * - Inherits all standard CRUD operations (save, findAll, findById, delete, etc.)
 * - T is the entity type (Product subclass)
 * - String is the ID type (MongoDB uses String IDs)
 * *
 * CUSTOM QUERY EXPLANATION:
 * - @Query("{ '_class' : ?0 }"): Creates a MongoDB query that filters by the _class field
 * - ?0 represents the first parameter (typeAlias)
 * - This query runs at DATABASE LEVEL, not in Java memory
 * - Only returns documents matching the specified class type
 * - Much more efficient than loading all products and filtering in Java
 * *
 * EXAMPLE USAGE:
 * - findByProductType("com.example.security.models.Collar") returns
 * only Collar documents
 * - This leverages MongoDB's automatic _class field that tracks the
 * Java class of each document
 */
@Repository
public interface ProductRepository<T extends Product> extends MongoRepository<T, String> {
    @Query("{ '_class' : ?0 }")
    List<Product> findByProductType(String typeAlias);
}
