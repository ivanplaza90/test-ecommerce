package com.ivan.test.ecommerce.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSize {
    private int sizeId;
    private int quantity;
    private boolean backSoon;
    private boolean special;
}
