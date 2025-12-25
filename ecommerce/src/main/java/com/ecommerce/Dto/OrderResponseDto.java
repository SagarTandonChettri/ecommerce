package com.ecommerce.Dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class OrderResponseDto {
    private String orderNumber;
    private String userId;

    private List<OrderItemResponseDto> items;

    private double totalAmount;
    private String status;
    private Instant createdAt;
}
