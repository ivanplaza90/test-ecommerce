package com.ivan.test.ecommerce.infrastructure.data.mongo;

import com.ivan.test.ecommerce.infrastructure.data.mongo.model.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductMongoRepository extends MongoRepository<ProductEntity, String> {
}
