package com.ecommerce.ApiResponse;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CreateProductResponse {

    private String productCode;
    private String name;
    private String description;
    private double price;
    private Date createdAt;
    private String imageType;
}
