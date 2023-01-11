package com.ivan.test.ecommerce.infrstructure.data.mongo;

import com.ivan.test.ecommerce.infrstructure.data.mongo.model.StockEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockMongoRepository extends MongoRepository<StockEntity, Integer> {
    StockEntity findBySizeId(Integer sizeId);
}
