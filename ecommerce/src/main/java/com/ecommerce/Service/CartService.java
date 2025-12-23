package com.ecommerce.Service;

import com.ecommerce.Entity.Cart;
import com.ecommerce.Entity.CartItem;
import com.ecommerce.Entity.Product;
import com.ecommerce.Exception.CartException;
import com.ecommerce.Exception.CartNotFoundException;
import com.ecommerce.Exception.InvalidQuantityException;
import com.ecommerce.Exception.ProductNotFoundException;
import com.ecommerce.Repository.CartRepository;
import com.ecommerce.Repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // ADD TO CART - Your exact logic, just with try-catch
    public Cart addToCart(String userId, String productCode, int quantity) {

        log.info("Add to cart request. userId={}, productCode={}, quantity={}",
                userId, productCode, quantity);

        try {
            // Your exact validation
            if (quantity <= 0) {
                throw new InvalidQuantityException("Quantity must be greater than zero");
            }

            // Your exact product fetch
            Product product = productRepository.findByProductCode(productCode)
                    .orElseThrow(() ->
                            new ProductNotFoundException(
                                    "Product with code '" + productCode + "' not found"
                            )
                    );

            // Your exact cart fetch/create logic
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseGet(() -> {
                        log.info("No cart found for user. Creating new cart. userId={}", userId);
                        return Cart.builder()
                                .userId(userId)
                                .items(new ArrayList<CartItem>())
                                .updatedAt(Instant.now())
                                .build();
                    });

            // Your exact item check logic
            Optional<CartItem> existingItemOpt = cart.getItems().stream()
                    .filter(item -> item.getProductCode().equals(productCode))
                    .findFirst();

            if (existingItemOpt.isPresent()) {
                CartItem existingItem = existingItemOpt.get();
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                log.info("Updated quantity for productCode={} newQuantity={}",
                        productCode, existingItem.getQuantity());
            } else {
                CartItem newItem = CartItem.builder()
                        .productCode(product.getProductCode())
                        .quantity(quantity)
                        .name(product.getName())
                        .priceAtAdd(product.getPrice())
                        .imageType(product.getImageType())
                        .build();

                cart.getItems().add(newItem);
                log.info("Added new item to cart. productCode={}", productCode);
            }

            cart.setUpdatedAt(Instant.now());

            // Your exact save with proper exception handling
            try {
                return cartRepository.save(cart);
            } catch (Exception e) {
                log.error("Failed to save cart for user {}: {}", userId, e.getMessage());
                throw new CartException("Failed to save cart to database");
            }

        } catch (ProductNotFoundException | InvalidQuantityException e) {
            // Re-throw these so GlobalExceptionHandler catches them
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error in addToCart: {}", e.getMessage(), e);
            throw new CartException("Failed to add item to cart");
        }
    }

    public Cart deleteCart(String userId, String productCode){

        log.info("Remove item from cart request. userId={}, productCode={}",
                userId, productCode);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow( () -> {
                    log.warn("Cart not found for userId = {}", userId);
                    return new CartNotFoundException(
                            "Cart not found for userId: "+ userId
                    );
                });

        boolean removed = cart.getItems().removeIf(
                item -> item.getProductCode().equals(productCode)
        );
        if (!removed) {
            log.warn("Product not found in cart. productCode={}", productCode);
            throw new CartException(
                    "Product with code '" + productCode + "' not found in cart"
            );
        }

        cart.setUpdatedAt(Instant.now());

        try {
            Cart savedCart = cartRepository.save(cart);
            log.info("Item removed successfully. productCode={}", productCode);
            return savedCart;
        } catch (Exception e) {
            log.error("Failed to update cart after removal: {}", e.getMessage());
            throw new CartException("Failed to remove item from cart");
        }
    }

    public Cart getCart(String userId){

        log.info("Fetching Cart by userId: {}",userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow( () -> {
                   log.warn("Cart not found for userId = {}",userId);
                   return new CartNotFoundException(
                           "Cart not found for userId: "+userId
                   );
                });

        log.info("Cart fetched successfully. userId={}, itemsCount={}",
                userId, cart.getItems().size());

        return cart;
    }
}
