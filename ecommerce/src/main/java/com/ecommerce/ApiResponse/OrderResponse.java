package com.ecommerce.ApiResponse;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private String orderNumber;
    private String userId;

    private List<OrderItemResponse> items;

    private double totalAmount;
    private String status;
    private Instant createdAt;
}
