package com.ecommerce.Controller;

import com.ecommerce.ApiResponse.ApiResponse;
import com.ecommerce.ApiResponse.CreateProductResponse;
import com.ecommerce.Dto.ProductDetailDTO;
import com.ecommerce.Dto.ProductListDto;
import com.ecommerce.Entity.Product;
import com.ecommerce.Exception.ImageValidationException;
import com.ecommerce.Exception.ProductAlreadyExistsException;
import com.ecommerce.Exception.ProductNotFoundException;
import com.ecommerce.Service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    // CREATE PRODUCT (ADMIN ONLY)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createProduct(
            @RequestParam String productCode,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam MultipartFile image) {

        log.info("Create product request received. productCode={}, name={}, price={}",
                productCode, name, price);

        Product savedProduct = productService.createProduct(
                productCode, name, description, price, image);

        log.info("Product created successfully. productCode={}",savedProduct.getProductCode());

        CreateProductResponse productResponse = CreateProductResponse.builder()
                .productCode(savedProduct.getProductCode())
                .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .imageType(savedProduct.getImageType())
                .price(savedProduct.getPrice())
                .createdAt(savedProduct.getCreatedAt())
                .build();

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .statusCode(201)
                .message("Product created successfully")
                .data(productResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET PRODUCT DETAIL
    @GetMapping("/{productCode}")
    public ResponseEntity<ApiResponse> getProductDetail(@PathVariable String productCode) {

        log.info("Fetch product detail request received. productCode={}", productCode);
        ProductDetailDTO product = productService.getProductDetail(productCode);

        log.info("Product detail fetched successfully. productCode={}", productCode);

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .statusCode(200)
                .message("Product retrieved successfully")
                .data(product)
                .build();
        return ResponseEntity.ok(response);
    }

    // GET ALL PRODUCTS (PUBLIC)
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {

        log.info("Fetch all products request received");

        List<ProductListDto> products = productService.getAllProducts();

        log.info("Fetched {} product(s)", products.size());

        String message = products.isEmpty() ?
                "No products available" :
                String.format("Found %d product%s", products.size(), products.size() == 1 ? "" : "s");

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .statusCode(200)
                .message(message)
                .data(products)
                .build();

        return ResponseEntity.ok(response);
    }

    // DELETE PRODUCT (ADMIN ONLY)
    @DeleteMapping("/delete/{productCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productCode) {

        log.info("Delete product request received. productCode={}", productCode);

        productService.deleteProduct(productCode);

        log.info("Product deleted successfully. productCode={}", productCode);

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .statusCode(200)
                .message("Product deleted successfully")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }



}
