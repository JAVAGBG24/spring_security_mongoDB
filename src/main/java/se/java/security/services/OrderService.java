package se.java.security.services;

import org.springframework.stereotype.Service;
import se.java.security.dto.OrderDTO;
import se.java.security.dto.OrderItemDTO;
import se.java.security.models.Order;
import se.java.security.models.Product;
import se.java.security.models.User;
import se.java.security.repository.OrderRepository;
import se.java.security.repository.ProductRepository;
import se.java.security.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // skapa en ny order
    public Order createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // tom lista
        List<Product> products = new ArrayList<>();
        Map<String, Integer> quantities = new HashMap<>();
        double totalAmount = 0.0;

        // gå igenom varenda orderrad (OrderItemDTO) i beställningen
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {

        }
    }





















}
