package com.ecommerce.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductListDto {
    private String productCode;
    private String name;
    private String description;
    private double price;
}
