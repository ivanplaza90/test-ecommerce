package com.ivan.test.ecommerce.infrastructure.data.mongo;

import com.ivan.test.ecommerce.domain.model.Product;
import com.ivan.test.ecommerce.infrastructure.data.mongo.model.ProductEntity;
import com.ivan.test.ecommerce.infrastructure.data.mongo.model.SizeEntity;
import com.ivan.test.ecommerce.infrastructure.data.mongo.model.StockEntity;
import com.ivan.test.ecommerce.infrastructure.data.mongo.mapper.EntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {

    private static final int PRODUCT_ID = 1;
    private static final int POSITION = 0;
    private static final int SIZE_ID = 1;
    private static final int QUANTITY = 10;
    @Mock
    private ProductMongoRepository productMongoRepository;
    @Mock
    private SizeMongoRepository sizeMongoRepository;
    @Mock
    private StockMongoRepository stockMongoRepository;

    @Spy
    private EntityMapper entityMapper = Mappers.getMapper(EntityMapper.class);
    @InjectMocks
    private ProductRepositoryImpl productRepositoryImpl;;

    @Test
    void should_throws_an_exception_given_a_product_id_when_mongo_repository_fails_while_get_a_product() {
        //GIVEN
        assertThat(productRepositoryImpl).isNotNull();
        final RuntimeException repositoryException = new RuntimeException("UNIT TEST");

        given(productMongoRepository.findByProductId(PRODUCT_ID)).willThrow(repositoryException);

        //WHEN
        final Throwable throwable = catchThrowable(() -> productRepositoryImpl.getProduct(PRODUCT_ID));

        //THEN
        assertThat(throwable).isNotNull()
            .isEqualTo(repositoryException);
        then(productMongoRepository).should().findByProductId(eq(PRODUCT_ID));
    }

    @Test
    void should_return_empty_given_a_product_id_when_product_repository_returns_empty_product() {
        //GIVEN
        given(productMongoRepository.findByProductId(PRODUCT_ID)).willReturn(Optional.empty());

        //WHEN
        final Optional<Product> response = productRepositoryImpl.getProduct(PRODUCT_ID);

        //THEN
        assertThat(response).isNotNull().isEmpty();
        then(productMongoRepository).should().findByProductId(eq(PRODUCT_ID));
    }

    @Test
    void should_throws_an_exception_given_a_product_id_when_mongo_repository_fails_while_get_product_size() {
        //GIVEN
        final RuntimeException repositoryException = new RuntimeException("UNIT TEST");
        given(productMongoRepository.findByProductId(PRODUCT_ID)).willReturn(Optional.of(mockProduct()));
        given(sizeMongoRepository.findByProductId(PRODUCT_ID)).willThrow(repositoryException);

        //WHEN
        final Throwable throwable = catchThrowable(() -> productRepositoryImpl.getProduct(PRODUCT_ID));

        //THEN
        assertThat(throwable).isNotNull()
                .isEqualTo(repositoryException);
        then(productMongoRepository).should().findByProductId(eq(PRODUCT_ID));
        then(sizeMongoRepository).should().findByProductId(PRODUCT_ID);
    }

    @Test
    void should_return_product_given_a_product_id_when_mongo_returns_a_product_and_empty_list_of_sizes_then_the_product_has_empty_sizes() {
        //GIVEN
        final ProductEntity storedProduct = mockProduct();
        final List<SizeEntity> storedSizes = Collections.emptyList();

        given(productMongoRepository.findByProductId(PRODUCT_ID)).willReturn(Optional.of(storedProduct));
        given(sizeMongoRepository.findByProductId(PRODUCT_ID)).willReturn(storedSizes);

        //WHEN
        final Optional<Product> response = productRepositoryImpl.getProduct(PRODUCT_ID);

        //THEN
        assertThat(response).isNotNull().isNotEmpty().get()
            .hasFieldOrPropertyWithValue("productId", PRODUCT_ID)
            .hasFieldOrPropertyWithValue("position", POSITION)
            .hasFieldOrProperty("sizes")
            .extracting("sizes").isNotNull().asList().isEmpty();

        then(productMongoRepository).should().findByProductId(eq(PRODUCT_ID));
        then(sizeMongoRepository).should().findByProductId(PRODUCT_ID);
        then(entityMapper).should().mapToProduct(eq(storedProduct));
        then(entityMapper).shouldHaveNoMoreInteractions();
    }

    @Test
    void should_throws_an_exception_given_a_product_id_when_mongo_repository_fails_while_get_product_size_stock() {
        //GIVEN
        final ProductEntity storedProduct = mockProduct();
        final List<SizeEntity> storedSizes = List.of(mockSizeEntity());
        final RuntimeException repositoryException = new RuntimeException("UNIT TEST");

        given(productMongoRepository.findByProductId(PRODUCT_ID)).willReturn(Optional.of(storedProduct));
        given(sizeMongoRepository.findByProductId(PRODUCT_ID)).willReturn(storedSizes);
        given(stockMongoRepository.findBySizeId(SIZE_ID)).willThrow(repositoryException);

        //WHEN
        final Throwable throwable = catchThrowable(() -> productRepositoryImpl.getProduct(PRODUCT_ID));

        //THEN
        assertThat(throwable).isNotNull()
                .isEqualTo(repositoryException);

        then(productMongoRepository).should().findByProductId(eq(PRODUCT_ID));
        then(sizeMongoRepository).should().findByProductId(PRODUCT_ID);
        then(stockMongoRepository).should().findBySizeId(SIZE_ID);
        then(entityMapper).should().mapToProduct(eq(storedProduct));
        then(entityMapper).shouldHaveNoMoreInteractions();
    }

    @Test
    void should_return_a_product_given_a_product_id_when_mongo_a_product_size_and_stock_is_stored() {
        //GIVEN
        final ProductEntity storedProduct = mockProduct();
        final List<SizeEntity> storedSizes = List.of(mockSizeEntity());
        final StockEntity stockEntity = mockStockEntity();

        given(productMongoRepository.findByProductId(PRODUCT_ID)).willReturn(Optional.of(storedProduct));
        given(sizeMongoRepository.findByProductId(PRODUCT_ID)).willReturn(storedSizes);
        given(stockMongoRepository.findBySizeId(SIZE_ID)).willReturn(stockEntity);

        //WHEN
        final Optional<Product> response = productRepositoryImpl.getProduct(PRODUCT_ID);

        //THEN
        assertThat(response).isNotNull().isNotEmpty().get()
                .hasFieldOrPropertyWithValue("productId", PRODUCT_ID)
                .hasFieldOrPropertyWithValue("position", POSITION)
                .hasFieldOrProperty("sizes")
                .extracting("sizes").isNotNull().asList().hasSize(1).first()
                .hasFieldOrPropertyWithValue("sizeId", SIZE_ID)
                .hasFieldOrPropertyWithValue("quantity", QUANTITY)
                .hasFieldOrPropertyWithValue("backSoon", false)
                .hasFieldOrPropertyWithValue("special", false);

        then(productMongoRepository).should().findByProductId(eq(PRODUCT_ID));
        then(sizeMongoRepository).should().findByProductId(PRODUCT_ID);
        then(stockMongoRepository).should().findBySizeId(SIZE_ID);
        then(entityMapper).should().mapToProduct(eq(storedProduct));
        then(entityMapper).should().mapToProductSize(eq(storedSizes.get(0)), eq(stockEntity));
    }

    private ProductEntity mockProduct() {
        return ProductEntity.builder()
            .productId(PRODUCT_ID)
            .position(POSITION)
            .build();
    }

    private SizeEntity mockSizeEntity() {
        return SizeEntity.builder()
            .productId(PRODUCT_ID)
            .sizeId(SIZE_ID)
            .backSoon(false)
            .special(false)
            .build();
    }

    private StockEntity mockStockEntity() {
        return StockEntity.builder()
            .sizeId(SIZE_ID)
            .quantity(QUANTITY)
            .build();
    }
}
