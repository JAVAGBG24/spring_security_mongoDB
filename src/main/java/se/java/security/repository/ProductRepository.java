package se.java.security.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import se.java.security.models.Product;

/**
 * MAIN REPOSITORY INTERFACE - Kombinerar Spring Data + Custom funktionalitet
 * 2. CustomProductRepository:
 *    - Våra egna komplexa queries som vi implementerar manuellt
 *    - Dynamiska filter, criteria queries, etc.
 * SPRING DATA MAGIC:
 * Spring Data MongoDB kommer automatiskt hitta CustomProductRepositoryImpl
 * och kombinera den med den autogenererade MongoRepository implementationen.
 * Namnkonvention: InterfaceName + "Impl" = automatisk upptäckt
 */
public interface ProductRepository extends MongoRepository<Product, String>, CustomProductRepository {

}