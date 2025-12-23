package com.ecommerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    private String id;

    // from JWT (username or userId)
    private String userId;

    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    @Builder.Default
    private Instant updatedAt = Instant.now();
}
