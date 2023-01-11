package com.ivan.test.ecommerce.application;

import com.ivan.test.ecommerce.domain.ProductRepository;
import com.ivan.test.ecommerce.domain.exception.EcommerceException;
import com.ivan.test.ecommerce.domain.exception.ProductNotFoundException;
import com.ivan.test.ecommerce.domain.exception.ProductWithoutSizesException;
import com.ivan.test.ecommerce.domain.exception.ProductWithoutStockException;
import com.ivan.test.ecommerce.domain.model.Product;
import com.ivan.test.ecommerce.domain.model.ProductSize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class GetProductWithStockTest {

    private static final Integer PRODUCT_ID = 1;
    public static final int POSITION = 0;

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private GetProductWithStock getProductWithStock;

    @Test
    void should_throws_an_exception_given_a_product_id_when_product_repository_throws_an_exception() {
        //GIVEN
        assertThat(getProductWithStock).isNotNull();
        given(productRepository.getProduct(PRODUCT_ID)).willThrow(new RuntimeException("UNIT TEST"));

        //WHEN
        final Throwable throwable = catchThrowable(() -> getProductWithStock.apply(PRODUCT_ID));

        //THEN
        assertThat(throwable).isNotNull()
                .isInstanceOf(EcommerceException.class);

        then(productRepository).should().getProduct(eq(PRODUCT_ID));

    }

    @Test
    void should_throws_product_not_found_exception_given_a_product_id_when_product_repository_returns_empty_result() {
        //GIVEN
        assertThat(getProductWithStock).isNotNull();
        given(productRepository.getProduct(PRODUCT_ID)).willReturn(Optional.empty());

        //WHEN
        final Throwable throwable = catchThrowable(() -> getProductWithStock.apply(PRODUCT_ID));

        //THEN
        assertThat(throwable).isNotNull()
                .isInstanceOf(ProductNotFoundException.class);

        then(productRepository).should().getProduct(eq(PRODUCT_ID));
    }

    @Test
    void should_throws_product_without_sizes_exception_given_a_product_id_when_product_repository_returns_a_product_without_any_size() {
        //GIVEN
        assertThat(getProductWithStock).isNotNull();

        given(productRepository.getProduct(PRODUCT_ID))
                .willReturn(Optional.of(mockStoredProduct(null)));

        //WHEN
        final Throwable throwable = catchThrowable(() -> getProductWithStock.apply(PRODUCT_ID));

        //THEN
        assertThat(throwable).isNotNull()
                .isInstanceOf(ProductWithoutSizesException.class);

        then(productRepository).should().getProduct(eq(PRODUCT_ID));
    }

    @Test
    void should_throws_product_without_stock_exception_given_a_product_id_when_product_repository_returns_a_product_with_sizes_and_each_size_has_not_stock() {
        //GIVEN
        assertThat(getProductWithStock).isNotNull();
        final Product productWithoutStock = mockStoredProduct(List.of(
                mockProductSizeWithoutStock(1),
                mockProductSizeWithoutStock(2)));

        given(productRepository.getProduct(PRODUCT_ID))
                .willReturn(Optional.of(productWithoutStock));

        //WHEN
        final Throwable throwable = catchThrowable(() -> getProductWithStock.apply(PRODUCT_ID));

        //THEN
        assertThat(throwable).isNotNull()
                .isInstanceOf(ProductWithoutStockException.class);

        then(productRepository).should().getProduct(eq(PRODUCT_ID));
    }

    @Test
    void should_return_a_product_given_a_product_id_when_the_stored_product_has_a_size_with_backSoon_state() {
        //GIVEN
        assertThat(getProductWithStock).isNotNull();
        final Product productWithIsBackSoonSize = mockStoredProduct(List.of(
                mockProductSizeWithoutStock(1),
                mockBackSoonProductSize(2)));

        given(productRepository.getProduct(PRODUCT_ID))
                .willReturn(Optional.of(productWithIsBackSoonSize));

        //WHEN
        final Product response = getProductWithStock.apply(PRODUCT_ID);

        //THEN
        assertThat(response).isNotNull()
            .hasFieldOrPropertyWithValue("productId", PRODUCT_ID)
            .hasFieldOrPropertyWithValue("position", POSITION)
            .hasFieldOrProperty("sizes").isNotNull()
            .extracting("sizes").isNotNull().asList().hasSize(1)
            .first().isNotNull()
            .hasFieldOrPropertyWithValue("sizeId", 2)
            .hasFieldOrPropertyWithValue("quantity", 0)
            .hasFieldOrPropertyWithValue("backSoon", true)
            .hasFieldOrPropertyWithValue("special", false);

        then(productRepository).should().getProduct(eq(PRODUCT_ID));
    }

    @Test
    void should_throws_product_without_stock_exception_given_a_product_id_when_product_repository_returns_a_product_with_special_size_and_the_rest_of_sizes_has_no_stock() {
        //GIVEN
        assertThat(getProductWithStock).isNotNull();
        final Product productWithSpecialSize = mockStoredProduct(List.of(
                mockProductSizeWithoutStock(1),
                mockSpecialProductSize(2)));

        given(productRepository.getProduct(PRODUCT_ID))
                .willReturn(Optional.of(productWithSpecialSize));

        //WHEN
        final Throwable throwable = catchThrowable(() -> getProductWithStock.apply(PRODUCT_ID));

        //THEN
        assertThat(throwable).isNotNull()
                .isInstanceOf(ProductWithoutStockException.class);

        then(productRepository).should().getProduct(eq(PRODUCT_ID));
    }

    @Test
    void should_return_product_given_a_product_id_when_the_stored_product_has_an_special_size_and_another_with_stock() {
        //GIVEN
        assertThat(getProductWithStock).isNotNull();
        final Product productWithSpecialAndStockSize = mockStoredProduct(List.of(
                mockProductSizeWithStock(1),
                mockSpecialProductSize(2)));

        given(productRepository.getProduct(PRODUCT_ID))
                .willReturn(Optional.of(productWithSpecialAndStockSize));

        //WHEN
        final Product response = getProductWithStock.apply(PRODUCT_ID);

        //THEN
        assertThat(response).isNotNull()
                .hasFieldOrPropertyWithValue("productId", PRODUCT_ID)
                .hasFieldOrPropertyWithValue("position", POSITION)
                .hasFieldOrProperty("sizes").isNotNull()
                .extracting("sizes").isNotNull().asList().hasSize(2);

        then(productRepository).should().getProduct(eq(PRODUCT_ID));
    }

    @Test
    void should_return_product_given_a_product_id_when_the_stored_product_has_an_special_size_and_another_with_back_soon() {
        //GIVEN
        assertThat(getProductWithStock).isNotNull();
        final Product productWithSpecialAndBackSoon = mockStoredProduct(List.of(
                mockBackSoonProductSize(1),
                mockSpecialProductSize(2)));

        given(productRepository.getProduct(PRODUCT_ID))
                .willReturn(Optional.of(productWithSpecialAndBackSoon));

        //WHEN
        final Product response = getProductWithStock.apply(PRODUCT_ID);

        //THEN
        assertThat(response).isNotNull()
                .hasFieldOrPropertyWithValue("productId", PRODUCT_ID)
                .hasFieldOrPropertyWithValue("position", POSITION)
                .hasFieldOrProperty("sizes").isNotNull()
                .extracting("sizes").isNotNull().asList().hasSize(2);

        then(productRepository).should().getProduct(eq(PRODUCT_ID));
    }

    private static Product mockStoredProduct(List<ProductSize> sizes) {
        return Product.builder()
            .productId(PRODUCT_ID)
            .position(POSITION)
            .sizes(sizes)
            .build();
    }

    private static ProductSize mockProductSizeWithoutStock(int productId) {
        return ProductSize.builder()
            .sizeId(productId)
            .backSoon(false)
            .special(false)
            .quantity(0)
            .build();
    }

    private static ProductSize mockProductSizeWithStock(int productId) {
        return ProductSize.builder()
                .sizeId(productId)
                .backSoon(false)
                .special(false)
                .quantity(10)
                .build();
    }

    private static ProductSize mockBackSoonProductSize(int productId) {
        return ProductSize.builder()
                .sizeId(productId)
                .backSoon(true)
                .special(false)
                .quantity(0)
                .build();
    }

    private static ProductSize mockSpecialProductSize(int productId) {
        return ProductSize.builder()
                .sizeId(productId)
                .backSoon(false)
                .special(true)
                .quantity(10)
                .build();
    }
}
