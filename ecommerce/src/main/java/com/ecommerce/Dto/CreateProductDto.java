package com.ecommerce.Dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CreateProductDto {

    private String productCode;
    private String name;
    private String description;
    private double price;
    private Date createdAt;
    private String imageType;
}
