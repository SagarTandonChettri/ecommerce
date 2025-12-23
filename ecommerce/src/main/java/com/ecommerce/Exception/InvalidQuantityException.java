package com.ecommerce.Exception;

public class InvalidQuantityException extends CartException{
    public InvalidQuantityException(String message){
        super(message);
    }
}
