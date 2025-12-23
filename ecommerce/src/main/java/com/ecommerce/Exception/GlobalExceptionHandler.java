package com.ecommerce.Exception;

import com.ecommerce.ApiResponse.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 📁 handler/GlobalExceptionHandler.java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //  SECURITY EXCEPTIONS
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(403)  // Forbidden
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
                .statusCode(401)  // Unauthorized
                .message("Authentication failed: Invalid or missing token")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleProductAlreadyExists(ProductAlreadyExistsException ex) {
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(409)  // Conflict
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse> handleProductNotFound(ProductNotFoundException ex) {
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(404)  // Not Found
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ImageValidationException.class)
    public ResponseEntity<ApiResponse> handleImageValidation(ImageValidationException ex) {
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(400)  // Bad Request
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneralException(Exception ex) {
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(500)
                .message("An unexpected error occurred")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    // ========== CART EXCEPTIONS ==========
    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<ApiResponse> handleInvalidQuantity(InvalidQuantityException ex) {
        log.warn("Invalid quantity: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(400)  // Bad Request
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCartNotFound(CartNotFoundException ex) {
        log.warn("Cart not found: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(404)  // Not Found
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CartException.class)
    public ResponseEntity<ApiResponse> handleCartException(CartException ex) {
        log.warn("Cart error: {}", ex.getMessage());
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .statusCode(400)  // Bad Request
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}