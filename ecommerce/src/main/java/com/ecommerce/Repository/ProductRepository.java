package com.ecommerce.Repository;

import com.ecommerce.Entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByProductCode(String productCode);

    boolean existsByProductCode(String productCode);

    @Query(value = "{}", fields = "{ 'imageData': 0 }")
    List<Product> findAllWithoutImages();

}
