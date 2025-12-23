package com.ecommerce.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDetailDTO {

    private String productCode;
    private String name;
    private String description;
    private double price;

    //Image returned ONLY here
    private byte[] imageData;
    private String imageType;
}
