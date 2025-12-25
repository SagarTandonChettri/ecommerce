package com.ecommerce.Service;

import com.ecommerce.ApiResponse.OrderItemResponse;
import com.ecommerce.ApiResponse.OrderResponse;
import com.ecommerce.Entity.Cart;
import com.ecommerce.Entity.Order;
import com.ecommerce.Entity.OrderItem;
import com.ecommerce.Exception.CartNotFoundException;
import com.ecommerce.Exception.EmptyCartException;
import com.ecommerce.Exception.OrderCreationException;
import com.ecommerce.Exception.OrderNotFoundException;
import com.ecommerce.OrderStatus;
import com.ecommerce.Repository.CartRepository;
import com.ecommerce.Repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderResponse> getOrderHistory(String userId){
        log.info("Request - Fetch All User Order for userId: {}",userId);

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);

        if (orders.isEmpty()){
            log.warn("No orders found for userId={}",userId);
            throw new OrderNotFoundException("Order History is empty for userId"+ userId);
        }

        return orders.stream()
                .map(order -> OrderResponse.builder()
                        .orderNumber(order.getOrderNumber())
                        .userId(order.getUserId())
                        .items(order.getItems().stream()
                                .map(item -> OrderItemResponse.builder()
                                        .productCode(item.getProductCode())
                                        .name(item.getName())
                                        .price(item.getPrice())
                                        .quantity(item.getQuantity())
                                        .totalPrice(item.getTotalPrice())
                                        .build())
                                .toList())
                        .totalAmount(order.getTotalAmount())
                        .status(order.getStatus().name())
                        .createdAt(order.getCreatedAt())
                        .build())
                .toList();
    }

    public OrderResponse createOrder (String userId){

        log.info("Create order for userId = {}",userId);

        // 1. Fetch cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Cart not found for userId={}", userId);
                    return new CartNotFoundException(
                            "Cart not found for userId: " + userId
                    );
                });

        // 2. Validate cart not empty
        if (cart.getItems().isEmpty()) {
            log.warn("Cart is empty for userId={}", userId);
            throw new EmptyCartException(
                    "Cart is empty. Cannot place order"
            );
        }

        // 3. Convert CartItem → OrderItem
        List<OrderItemResponse> orderItems = cart.getItems().stream()
                .map(item -> {
                   double itemTotal = item.getPriceAtAdd() * item.getQuantity();
                   return OrderItemResponse.builder()
                           .productCode(item.getProductCode())
                           .name(item.getName())
                           .price(item.getPriceAtAdd())
                           .quantity(item.getQuantity())
                           .totalPrice(itemTotal)
                           .build();
                })
                .collect(Collectors.toList());

        // 4. Calculate total order amount
        double totalAmount = orderItems.stream()
                .mapToDouble(OrderItemResponse::getTotalPrice)
                .sum();

        // 5. Generate order number
        String orderNumber = generateOrderNumber();

        // 6. Create order entity for database (with IDs)
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .userId(userId)
                .items(orderItems.stream()
                        .map(dto -> OrderItem.builder()
                                .productCode(dto.getProductCode())
                                .name(dto.getName())
                                .price(dto.getPrice())
                                .quantity(dto.getQuantity())
                                .totalPrice(dto.getTotalPrice())
                                .build())
                        .collect(Collectors.toList()))
                .totalAmount(totalAmount)
                .status(OrderStatus.CREATED)
                .createdAt(Instant.now())
                .build();

        // 6. Save order
        try {
            Order savedOrder = orderRepository.save(order);
            log.info("Order created successfully. orderNumber={}", savedOrder.getOrderNumber());

            cartRepository.delete(cart);
            log.info("Order created and cart cleared. orderNumber={}, userId={}",
                    orderNumber, userId);
        } catch (Exception e) {
            log.error("Failed to create order for userId={}", userId, e);
            throw new OrderCreationException("Failed to create order");
        }

        // RETURN DTO
        return OrderResponse.builder()
                .orderNumber(orderNumber)
                .userId(userId)
                .items(orderItems)
                .totalAmount(totalAmount)
                .status("CREATED")
                .createdAt(Instant.now())
                .build();
    }

    private String generateOrderNumber() {
        return "ORD-" + Instant.now().toEpochMilli();
    }

}
