package com.ecommerce.Exception;

import com.ecommerce.ApiResponse.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ========== SECURITY EXCEPTIONS (400s) ==========
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(403)
                .message("Access Denied: You don't have permission")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(401)
                .message("Authentication failed: Invalid or missing token")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // ========== PRODUCT EXCEPTIONS (400s) ==========
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleProductAlreadyExists(ProductAlreadyExistsException ex) {
        log.warn("Product already exists: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(409)
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse> handleProductNotFound(ProductNotFoundException ex) {
        log.warn("Product not found: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(404)
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ImageValidationException.class)
    public ResponseEntity<ApiResponse> handleImageValidation(ImageValidationException ex) {
        log.warn("Image validation failed: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(400)
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ========== CART EXCEPTIONS (400s) ==========
    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<ApiResponse> handleEmptyCartException(EmptyCartException ex) {
        log.warn("Cart is empty: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(400)
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCartNotFoundException(CartNotFoundException ex) {
        log.warn("Cart not found: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(404)
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<ApiResponse> handleInvalidQuantityException(InvalidQuantityException ex) {
        log.warn("Invalid quantity: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(400)
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CartException.class)
    public ResponseEntity<ApiResponse> handleCartException(CartException ex) {
        log.warn("Cart error: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(400)
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    //=========== ORDER EXCEPTION (400s) ===========
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponse> handleOrderNotFoundException(OrderNotFoundException ex){
        log.error("Failed to Fetch order History:{}",ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(404)
                .message("Failed to Fetch, Order Might Not Exist. Please Try Again.")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // ========== ORDER EXCEPTIONS (500s) ==========
    @ExceptionHandler(OrderCreationException.class)
    public ResponseEntity<ApiResponse> handleOrderCreationException(OrderCreationException ex) {
        log.error("Order creation failed: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(500)
                .message("Failed to create order. Please try again.")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ApiResponse> handleInvalidOrderState(InvalidOrderStateException ex) {
        log.warn("Invalid order state: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(409)
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // ========= PAYMENT EXCCEPTION ============
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handlePaymentException(Exception ex){
        log.error("Payment error: {}", ex.getMessage(), ex);
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(400)
                .message(ex.getMessage())
                .data(null)
                .build();
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}