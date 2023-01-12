package com.ivan.test.ecommerce.infrastructure.data.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SizeEntity {
    private Integer productId;
    private Integer sizeId;
    private Boolean backSoon;
    private Boolean special;
}
