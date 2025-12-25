package com.ecommerce.Entity;

import com.ecommerce.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    private String id;

    private String orderNumber;
    private String userId;

    private List<OrderItem> items;

    private double totalAmount;

    private OrderStatus status;
    private Instant createdAt;
}
