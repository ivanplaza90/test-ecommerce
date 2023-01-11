package com.ivan.test.ecommerce.infrstructure.data.mongo.mapper;

import com.ivan.test.ecommerce.domain.model.Product;
import com.ivan.test.ecommerce.domain.model.ProductSize;
import com.ivan.test.ecommerce.infrstructure.data.mongo.model.ProductEntity;
import com.ivan.test.ecommerce.infrstructure.data.mongo.model.SizeEntity;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", injectionStrategy = CONSTRUCTOR, unmappedTargetPolicy = IGNORE)
public interface EntityMapper {
    Product mapToProduct(ProductEntity productEntity);

    List<ProductSize> mapToProductSizes(List<SizeEntity> sizes);
}
