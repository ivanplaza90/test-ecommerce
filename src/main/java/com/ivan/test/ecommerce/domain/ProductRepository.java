package com.ivan.test.ecommerce.domain;

import com.ivan.test.ecommerce.domain.model.Product;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> getProduct(Integer productId);
}
