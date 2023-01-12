package com.ivan.test.ecommerce.infrastructure.data.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockEntity {
    private Integer sizeId;
    private Integer quantity;
}
