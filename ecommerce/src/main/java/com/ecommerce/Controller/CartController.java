package com.ecommerce.Controller;

import com.ecommerce.ApiResponse.ApiResponse;
import com.ecommerce.Entity.Cart;
import com.ecommerce.Service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    // ADD TO CART
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(
            @RequestParam String productCode,
            @RequestParam int quantity,
            @AuthenticationPrincipal String userId) {

        log.info("Add to cart request. userId={}, productCode={}, quantity={}",
                userId, productCode, quantity);

        Cart cart = cartService.addToCart(userId, productCode, quantity);

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .statusCode(200)
                .message("Item added to cart successfully")
                .data(cart)
                .build();

        return ResponseEntity.ok(response);
    }

    // GET CART
    @GetMapping
    public ResponseEntity<ApiResponse> getCart(@AuthenticationPrincipal String userId) {

        log.info("Get cart request. userId={}", userId);

        Cart cart = cartService.getCart(userId);

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .statusCode(200)
                .message("Cart retrieved successfully")
                .data(cart)
                .build();

        return ResponseEntity.ok(response);
    }
//
//    // UPDATE ITEM QUANTITY
//    @PutMapping("/update")
//    public ResponseEntity<ApiResponse> updateCartItem(
//            @RequestParam String productCode,
//            @RequestParam int quantity,
//            @AuthenticationPrincipal String userId) {
//
//        log.info("Update cart item request. userId={}, productCode={}, quantity={}",
//                userId, productCode, quantity);
//
//        Cart cart = cartService.updateCartItem(userId, productCode, quantity);
//
//        ApiResponse response = ApiResponse.builder()
//                .success(true)
//                .statusCode(200)
//                .message("Cart item updated successfully")
//                .data(cart)
//                .build();
//
//        return ResponseEntity.ok(response);
//    }
//
//    // REMOVE ITEM FROM CART
//    @DeleteMapping("/remove/{productCode}")
//    public ResponseEntity<ApiResponse> removeFromCart(
//            @PathVariable String productCode,
//            @AuthenticationPrincipal String userId) {
//
//        log.info("Remove from cart request. userId={}, productCode={}",
//                userId, productCode);
//
//        Cart cart = cartService.removeCartItem(userId, productCode);
//
//        String message = cart.getItems().isEmpty()
//                ? "Cart is now empty"
//                : "Item removed from cart";
//
//        ApiResponse response = ApiResponse.builder()
//                .success(true)
//                .statusCode(200)
//                .message(message)
//                .data(cart)
//                .build();
//
//        return ResponseEntity.ok(response);
//    }
//
    // CLEAR CART
    @DeleteMapping("/item/{productCode}")
    public ResponseEntity<ApiResponse> deleteCart(@AuthenticationPrincipal String userId,@PathVariable String productCode) {

        log.info("Remove cart item request. userId={}, productCode={}",
                userId, productCode);

        Cart cart = cartService.deleteCart(userId, productCode);

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .statusCode(200)
                .message("Item removed from cart successfully")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }
}
