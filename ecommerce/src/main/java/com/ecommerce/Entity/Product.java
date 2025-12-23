package com.ecommerce.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    private String productCode;
    private String name;
    private String description;
    private double price;

//    Image fields
    private byte[] imageData;
    private String imageType;

    private Date createdAt = new Date();

}
