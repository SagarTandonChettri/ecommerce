package com.ecommerce.Service;

import com.ecommerce.Dto.ProductDetailDTO;
import com.ecommerce.Dto.ProductListDto;
import com.ecommerce.Entity.Product;
import com.ecommerce.Exception.ImageValidationException;
import com.ecommerce.Exception.ProductAlreadyExistsException;
import com.ecommerce.Exception.ProductNotFoundException;
import com.ecommerce.Repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // GET ALL PRODUCTS (FAST - NO IMAGES)
    public List<ProductListDto> getAllProducts(){

        log.info("Getting all products");

        return productRepository.findAllWithoutImages()
                .stream()
                .map(p -> ProductListDto.builder()
                        .productCode(p.getProductCode())
                        .name(p.getName())
                        .description(p.getDescription())
                        .price(p.getPrice())
                        .build())
                .toList();
    }

    // DETAIL API (ONE REQUEST WITH IMAGE)
    public ProductDetailDTO getProductDetail(String productCode) {
        log.info("Getting product detail for code: {}", productCode);
        Product product = productRepository.findByProductCode(productCode)

                .orElseThrow(() -> {
                    log.warn("Product not found: {}", productCode);
                    return new ProductNotFoundException("Product with code '" + productCode + "' not found");
                });

        log.debug("Found product: {} - {}", productCode, product.getName());
        return ProductDetailDTO.builder()
                .productCode(product.getProductCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageData(product.getImageData())
                .imageType(product.getImageType())
                .build();
    }

    // CREATE PRODUCT (ADMIN ONLY)
    @ExceptionHandler
    public Product createProduct(String productCode, String name,
                                 String description, double price,
                                 MultipartFile image) {

        log.info("Creating product: {}", productCode);

        if (productRepository.existsByProductCode(productCode)) {
            log.warn("Product already exists: {}", productCode);
            throw new ProductAlreadyExistsException("Product with code '" + productCode + "' already exists");
        }

        log.debug("Validating image: {}", image.getOriginalFilename());

        // 2. Validate image format/size
        validateImage(image);

        // 3. Process image
        byte[] imageData;
        try {
            imageData = image.getBytes();
            log.debug("Image processed: {} bytes", imageData.length);
        } catch (IOException e) {
            log.error("Failed to read image bytes", e);
            throw new ImageValidationException("Failed to read image file");
        }

        // 4. Create and save product
        Product product = new Product();
        product.setProductCode(productCode);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageData(imageData);
        product.setImageType(image.getContentType());

        // 5. Save to database
        try {
            Product saved = productRepository.save(product);
            log.info("Product created successfully: {}", productCode);
            return saved;
        } catch (Exception e) {
            log.error("Failed to save product {}: {}", productCode, e.getMessage());
            throw new RuntimeException("Failed to save product to database");
        }
    }

    // DELETE PRODUCT
    public void deleteProduct(String productCode) {

        log.info("Deleting product. productCode={}", productCode);

        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product with code '" + productCode + "' not found"
                        )
                );

        productRepository.delete(product);

        log.info("Product deleted from database. productCode={}", productCode);
    }


    // IMAGE VALIDATION
    private void validateImage(MultipartFile image) {
        log.debug("Validating image - Size: {}, Type: {}", image.getSize(), image.getContentType());

        if (image.isEmpty()) {
            log.warn("Image is empty");
            throw new ImageValidationException("Image is required");
        }

        if (image.getSize() > 2 * 1024 * 1024) {
            log.warn("Image too large: {} bytes", image.getSize());
            throw new ImageValidationException("Image size exceeds 2MB limit");
        }

        String contentType = image.getContentType();
        List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/webp");
        if (!allowedTypes.contains(contentType)) {
            log.warn("Invalid image type: {}", contentType);
            throw new ImageValidationException("Invalid image type. Allowed: JPEG, PNG, WebP");
        }
        log.debug("Image validation passed");
    }

}
