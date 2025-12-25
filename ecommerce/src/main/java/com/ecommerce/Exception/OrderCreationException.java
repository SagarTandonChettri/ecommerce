package com.ecommerce.Exception;

public class OrderCreationException extends RuntimeException {
    public OrderCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderCreationException(String message) {
        super(message);
    }
}
