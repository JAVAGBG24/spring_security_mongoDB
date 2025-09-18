package se.java.security.repository;


import se.java.security.models.Product;

import java.util.List;
import java.util.Map;

/**
 * CUSTOM REPOSITORY INTERFACE - Utökar Spring Data funktionalitet
 * REPOSITORY PATTERN FÖRKLARING:
 * Repository Pattern abstraherar dataåtkomst och ger ett objekt-orienterat sätt
 * att interagera med databasen utan att business logic behöver veta om SQL/MongoDB/etc.
 * *
 * VARFÖR CUSTOM REPOSITORY:
 * Spring Data MongoDB ger oss grundläggande CRUD-operationer gratis via MongoRepository,
 * men för komplexa queries (som dynamiska filter) behöver vi skriva egen kod.
 * *
 * INTERFACE SEGREGATION PRINCIPLE:
 * Vi skapar ett separat interface för våra custom metoder istället för att
 * stoppa in allt i en stor interface. Detta gör koden mer modulär och testbar.
 * *
 * FÖRBEREDER FÖR STRATEGY PATTERN:
 * Interfacet förbereder för en filter-strategy som kommer implementeras senare.
 * Flexibla filter-parametrar möjliggör olika filtreringsstrategier utan kod-ändringar.
 */
public interface CustomProductRepository {
    /**
     * DYNAMIC QUERY METHOD - Flexibel produktsökning med kriterier
     * DESIGN DECISIONS FÖRKLARING:
     * 1. Map<String, String> filters:
     *    - Flexibelt: Kan hantera olika antal filter utan att ändra metod-signatur
     *    - Key = filter-namn ("minPrice", "color", "size")
     *    - Value = filter-värde ("100", "red", "M")
     *    - String för alla värden = enkel parsing från HTTP requests
     * *
     * 2. Class<? extends Product> productclass:
     *    - Typesäker polymorphism: Kan filtrera på specifika produkttyper
     *    - Exempel: söka endast bland Collar-objekt med size-filter
     *    - Bounded wildcard (? extends Product) = kan bara vara Product eller dess subklasser
     */
    List<Product> findProductByCriteria(Map<String, String> filters, Class<? extends Product> productclass);
}
