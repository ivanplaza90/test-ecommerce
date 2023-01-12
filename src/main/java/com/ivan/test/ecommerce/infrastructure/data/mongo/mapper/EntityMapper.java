package com.ivan.test.ecommerce.infrastructure.data.mongo.mapper;

import com.ivan.test.ecommerce.domain.model.Product;
import com.ivan.test.ecommerce.domain.model.ProductSize;
import com.ivan.test.ecommerce.infrastructure.data.mongo.model.ProductEntity;
import com.ivan.test.ecommerce.infrastructure.data.mongo.model.SizeEntity;
import com.ivan.test.ecommerce.infrastructure.data.mongo.model.StockEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", injectionStrategy = CONSTRUCTOR, unmappedTargetPolicy = IGNORE)
public interface EntityMapper {
    Product mapToProduct(ProductEntity productEntity);

    @Mapping(target = "sizeId", source = "size.sizeId")
    ProductSize mapToProductSize(SizeEntity size, StockEntity stock);
}
