package com.ivan.test.ecommerce.domain;

import com.ivan.test.ecommerce.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> getProducts();
}
