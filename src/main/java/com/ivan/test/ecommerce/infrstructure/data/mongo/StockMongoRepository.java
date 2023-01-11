package com.ivan.test.ecommerce.infrstructure.data.mongo;

import com.ivan.test.ecommerce.infrstructure.data.mongo.model.StockEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StockMongoRepository extends MongoRepository<StockEntity, Integer> {
    Optional<StockEntity> findBySizeId(Integer sizeId);
}
