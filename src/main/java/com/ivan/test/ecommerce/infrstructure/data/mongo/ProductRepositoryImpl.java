package com.ivan.test.ecommerce.infrstructure.data.mongo;

import com.ivan.test.ecommerce.domain.ProductRepository;
import com.ivan.test.ecommerce.domain.model.Product;
import com.ivan.test.ecommerce.infrstructure.data.mongo.mapper.EntityMapper;

import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {

    private ProductMongoRepository productMongoRepository;
    private SizeMongoRepository sizeMongoRepository;

    private StockMongoRepository stockMongoRepository;
    private EntityMapper entityMapper;
    @Override
    public Optional<Product> getProduct(Integer productId) {
        return productMongoRepository.findByProductId(productId)
            .map(productEntity ->
                    entityMapper.mapToProduct(productEntity, sizeMongoRepository.findByProductId(productId)));
    }
}
