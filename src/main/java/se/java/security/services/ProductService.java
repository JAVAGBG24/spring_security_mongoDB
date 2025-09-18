package se.java.security.services;

/**
 *GENERIC CRUD SERVICE CLASS
 * *
 *PURPOSE: This service provides basic CRUD (Create, Read, Update, Delete)
 * operations for any Product type. It acts as a business logic layer
 * between controllers and the repository.
 *  *
 *GENERIC TYPE PARAMETER:
 *- <T extends Product>: This service can work with any Product subclass
 *- Maintains type safety throughout the operation chain
 *- Example: ProductService<Collar> only works with Collar objects
 *  *
 * DEPENDENCY INJECTION:
 *- The generic type T is resolved at runtime based on how the service is instantiated
 */
/*
@Service
public class ProductService <T extends Product> {
    private final ProductRepository<T> productRepository;

    @Autowired
    public ProductService(ProductRepository<T> productRepository) {
        this.productRepository = productRepository;
    }

    // getAll
    public List<T> getAllProducts() {
        return productRepository.findAll();
    }

    // create
    public T createProduct(T product) {
        return productRepository.save(product);
    }

    // productById
    public Optional<T> getProductById(String id) {
        return productRepository.findById(id);
    }

    // update
    public T updateProduct(String id, T product) {
        if(!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }
        product.setId(id);
        return productRepository.save(product);
    }

    // delete
    public void deleteProduct(String id) {
        if(!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }
        productRepository.deleteById(id);
    }
}*/
