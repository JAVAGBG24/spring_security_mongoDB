package se.java.security.services;

import org.springframework.stereotype.Service;
import se.java.security.repository.OrderRepository;
import se.java.security.repository.ProductRepository;
import se.java.security.repository.UserRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

/*

    // skapa en ny order
    // byter typ när vi gjort OrderResponseDTO från Order
    public OrderResponseDTO createOrder(OrderDTO orderDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("User is not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // tom lista
        List<Product> products = new ArrayList<>();
        Map<String, Integer> quantities = new HashMap<>();
        double totalAmount = 0.0;

        // gå igenom varenda orderrad (OrderItemDTO) i beställningen
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + itemDTO.getProductId()));

            products.add(product);
            quantities.put(itemDTO.getProductId(), itemDTO.getQuantity());
            totalAmount += product.getPrice() * itemDTO.getQuantity();
        }

        Order newOrder = new Order();
        newOrder.setCustomer(user);
        newOrder.setItems(products);
        newOrder.setQuantities(quantities);
        newOrder.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(newOrder);

       // return orderRepository.save(newOrder);
        return convertToOrderResponseDTO(savedOrder);
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
       /* List<Order> orders = orderRepository.findAll();

        // konvertera order objekten till orderResponse objekt
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());*/
    /*
    }

    public List<OrderResponse> getUserOrders(String userId) {
        /*User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));*/
/*
        if(!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        List<Order> orders = orderRepository.findByCustomerId(userId);

        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private OrderResponseDTO convertToOrderResponseDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(product -> {
                    String productId = product.getId();
                    String name = product.getName();
                    double price = product.getPrice();
                    int quantity = order.getQuantities().getOrDefault(productId, 0);
                    return new OrderItemDTO(productId, name, price, quantity);
                })
                .collect(Collectors.toList());

        Map<String, String> quantities = order.getQuantities().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> productRepository.findById(entry.getKey())
                                .map(Product::getName)
                                .orElse("Product not found")
                ));
        return new OrderResponseDTO(
                order.getId(),
                order.getCustomer().getId(),
                order.getTotalAmount(),
                itemDTOs,
                quantities,
                order.getCreatedAt().toString()
        );
    }


    private OrderResponse convertToDTO(Order order) {
        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setUserId(order.getCustomer().getId());

        orderResponse.setOrderedProductIds(
                order.getItems().stream()
                        .map(Product::getId)
                        .collect(Collectors.toList())
        );

        return orderResponse;

    }


*/


















}
