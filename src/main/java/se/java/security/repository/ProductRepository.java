package se.java.security.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import se.java.security.models.Category;
import se.java.security.models.Product;
import se.java.security.models.Sizes;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByColor(String color);

    List<Product> findByCategory(Category category);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    // query to find products with a specific size in stock (quantity > 0)
    @Query("{ 'inventory.?0': { $gt: 0 } }")
    List<Product> findBySizeInStock(Sizes size);

    // query to find products with inventory in a specific size within a range
    @Query("{ 'inventory.?0': { $gte: ?1, $lte: ?2 } }")
    List<Product> findBySizeInventoryBetween(Sizes size, int min, int max);

    // query to find products by color and with a specific size in stock
    @Query("{ 'color': ?0, 'inventory.?1': { $gt: 0 } }")
    List<Product> findByColorAndSizeInStock(String color, Sizes size);

    // query to find products by category and with a specific size in stock
    @Query("{ 'category': ?0, 'inventory.?1': { $gt: 0 } }")
    List<Product> findByCategoryAndSizeInStock(Category category, Sizes size);

    // query to find products in a price range and with a specific size in stock
    @Query("{ 'price': { $gte: ?0, $lte: ?1 }, 'inventory.?2': { $gt: 0 } }")
    List<Product> findByPriceRangeAndSizeInStock(Double minPrice, Double maxPrice, Sizes size);
}
