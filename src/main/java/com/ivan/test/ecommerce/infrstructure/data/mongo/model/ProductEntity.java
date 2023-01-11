package com.ivan.test.ecommerce.infrstructure.data.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    private Integer productId;
    private Integer position;
}
