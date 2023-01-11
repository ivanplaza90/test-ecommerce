package com.ivan.test.ecommerce.infrstructure.data.mongo;

import com.ivan.test.ecommerce.infrstructure.data.mongo.model.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductMongoRepository extends MongoRepository<ProductEntity, String> {
    Optional<ProductEntity> findByProductId(Integer productId);
}
