package com.ivan.test.ecommerce.domain.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Integer productId;
    private Integer position;
    @With
    private List<ProductSize> sizes;
}
