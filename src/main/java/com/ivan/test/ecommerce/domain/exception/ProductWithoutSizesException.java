package com.ivan.test.ecommerce.domain.exception;

public class ProductWithoutSizesException extends EcommerceException{
    public ProductWithoutSizesException(String message) {
        super(message);
    }
}
