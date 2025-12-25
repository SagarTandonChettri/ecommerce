package com.ecommerce.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponseDto {
    private String productCode;
    private String name;
    private double price;
    private int quantity;
    private double totalPrice;
}
