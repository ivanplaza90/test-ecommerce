package com.ivan.test.ecommerce.domain.exception;

public class ProductWithoutStockException extends EcommerceException {
    public ProductWithoutStockException(String message) {
        super(message);
    }
}
