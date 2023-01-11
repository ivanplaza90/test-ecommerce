package com.ivan.test.ecommerce.domain.exception;

public class ProductNotFoundException extends EcommerceException{
    public ProductNotFoundException(String message) {
        super(message);
    }
}
