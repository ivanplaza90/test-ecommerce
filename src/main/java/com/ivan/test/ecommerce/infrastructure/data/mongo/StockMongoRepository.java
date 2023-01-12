package com.ivan.test.ecommerce.infrastructure.data.mongo;

import com.ivan.test.ecommerce.infrastructure.data.mongo.model.StockEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockMongoRepository extends MongoRepository<StockEntity, Integer> {
    StockEntity findBySizeId(Integer sizeId);
}
