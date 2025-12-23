package com.ecommerce.Exception;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String msg) {
        super(msg);
    }
}
