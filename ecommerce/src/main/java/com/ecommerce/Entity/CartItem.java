package com.ecommerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    // product reference
    private String productCode;

    // quantity user wants
    private int quantity;

    // snapshot fields
    private String name;
    private double priceAtAdd;
    private String imageType;
}
