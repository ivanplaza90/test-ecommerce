package com.ivan.test.ecommerce.infrastructure.data.mongo;

import com.ivan.test.ecommerce.domain.model.Product;
import com.ivan.test.ecommerce.infrastructure.data.mongo.mapper.EntityMapper;
import com.ivan.test.ecommerce.infrastructure.data.mongo.model.ProductEntity;
import com.ivan.test.ecommerce.infrastructure.data.mongo.model.SizeEntity;
import com.ivan.test.ecommerce.infrastructure.data.mongo.model.StockEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

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
    private ProductRepositoryImpl productRepositoryImpl;

    @Test
    void should_throws_an_exception_given_not_params_when_mongo_repository_fails_while_get_products() {
        //GIVEN
        assertThat(productRepositoryImpl).isNotNull();
        final RuntimeException repositoryException = new RuntimeException("UNIT TEST");

        given(productMongoRepository.findAll()).willThrow(repositoryException);

        //WHEN
        final Throwable throwable = catchThrowable(() -> productRepositoryImpl.getProducts());

        //THEN
        assertThat(throwable).isNotNull()
                .isEqualTo(repositoryException);
        then(productMongoRepository).should().findAll();
    }

    @Test
    void should_return_an_empty_list_given_not_params_when_mongo_repository_returns_empty_list() {
        //GIVEN
        assertThat(productRepositoryImpl).isNotNull();

        given(productMongoRepository.findAll()).willReturn(Collections.emptyList());

        //WHEN
        final List<Product> response = productRepositoryImpl.getProducts();

        //THEN
        assertThat(response).isNotNull().asList().isEmpty();
        then(productMongoRepository).should().findAll();
    }

    @Test
    void should_return_list_given_not_params_when_mongo_returns_a_product_and_not_sizes_then_the_list_has_the_element() {
        //GIVEN
        assertThat(productRepositoryImpl).isNotNull();

        final ProductEntity storedProduct = mockProduct();
        final List<SizeEntity> storedSizes = Collections.emptyList();

        given(productMongoRepository.findAll()).willReturn(List.of(storedProduct));
        given(sizeMongoRepository.findByProductId(PRODUCT_ID)).willReturn(storedSizes);

        //WHEN
        final List<Product> response = productRepositoryImpl.getProducts();

        //THEN
        assertThat(response).isNotNull().asList().hasSize(1)
            .element(0).isNotNull()
            .hasFieldOrPropertyWithValue("productId", PRODUCT_ID)
            .hasFieldOrPropertyWithValue("position", POSITION);

        then(productMongoRepository).should().findAll();
        then(entityMapper).should().mapToProduct(eq(storedProduct));
        then(sizeMongoRepository).should().findByProductId(eq(PRODUCT_ID));
    }

    @Test
    void should_return_list_given_not_params_when_mongo_returns_a_product_with_sizes_and_stock_then_the_list_has_the_element() {
        //GIVEN
        assertThat(productRepositoryImpl).isNotNull();

        final ProductEntity storedProduct = mockProduct();
        final List<SizeEntity> storedSizes = List.of(mockSizeEntity());
        final StockEntity stockEntity = mockStockEntity();

        given(productMongoRepository.findAll()).willReturn(List.of(storedProduct));
        given(sizeMongoRepository.findByProductId(PRODUCT_ID)).willReturn(storedSizes);
        given(stockMongoRepository.findBySizeId(SIZE_ID)).willReturn(stockEntity);

        //WHEN
        final List<Product> response = productRepositoryImpl.getProducts();

        //THEN
        assertThat(response).isNotNull().asList().hasSize(1)
                .element(0).isNotNull()
                .hasFieldOrPropertyWithValue("productId", PRODUCT_ID)
                .hasFieldOrPropertyWithValue("position", POSITION)
                .hasFieldOrProperty("sizes")
                .extracting("sizes").isNotNull().asList().hasSize(1).first()
                .hasFieldOrPropertyWithValue("sizeId", SIZE_ID)
                .hasFieldOrPropertyWithValue("quantity", QUANTITY)
                .hasFieldOrPropertyWithValue("backSoon", false)
                .hasFieldOrPropertyWithValue("special", false);

        then(productMongoRepository).should().findAll();
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
