package com.ecommerce.Exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String msg) {
        super(msg);
    }
}
