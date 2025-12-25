package com.ecommerce.ApiResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {
    private String productCode;
    private String name;
    private double price;
    private int quantity;
    private double totalPrice;
}
