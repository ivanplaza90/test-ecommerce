package com.ivan.test.ecommerce.infrastructure.data.mongo;

import com.ivan.test.ecommerce.infrastructure.data.mongo.model.SizeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SizeMongoRepository extends MongoRepository<SizeEntity, Integer> {
    List<SizeEntity> findByProductId(Integer productId);
}
