package com.ivan.test.ecommerce.infrastructure.data.mongo;

import com.ivan.test.ecommerce.domain.ProductRepository;
import com.ivan.test.ecommerce.domain.model.Product;
import com.ivan.test.ecommerce.domain.model.ProductSize;
import com.ivan.test.ecommerce.infrastructure.data.mongo.mapper.EntityMapper;
import com.ivan.test.ecommerce.infrastructure.data.mongo.model.SizeEntity;
import com.ivan.test.ecommerce.infrastructure.data.mongo.model.StockEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductRepositoryImpl implements ProductRepository {

    private ProductMongoRepository productMongoRepository;
    private SizeMongoRepository sizeMongoRepository;
    private StockMongoRepository stockMongoRepository;
    private EntityMapper entityMapper;
    @Override
    public Optional<Product> getProduct(Integer productId) {
        return productMongoRepository.findByProductId(productId)
            .map(entityMapper::mapToProduct)
            .map(product -> product.withSizes(getProductSizes(productId)));
    }

    private List<ProductSize> getProductSizes(Integer productId) {
        return sizeMongoRepository.findByProductId(productId).stream()
            .map(sizeEntity -> entityMapper.mapToProductSize(sizeEntity, getStockMongoRepositoryBySizeId(sizeEntity)))
            .collect(Collectors.toList());
    }

    private StockEntity getStockMongoRepositoryBySizeId(SizeEntity sizeEntity) {
        return stockMongoRepository.findBySizeId(sizeEntity.getSizeId());
    }
}