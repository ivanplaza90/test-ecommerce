package com.ivan.test.ecommerce.infrstructure.data.mongo;

import com.ivan.test.ecommerce.infrstructure.data.mongo.model.SizeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SizeMongoRepository extends MongoRepository<SizeEntity, Integer> {
    List<SizeEntity> findByProductId(Integer productId);
}
