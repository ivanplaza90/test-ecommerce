package com.ivan.test.ecommerce.application;

import com.ivan.test.ecommerce.domain.ProductRepository;
import com.ivan.test.ecommerce.domain.exception.EcommerceException;
import com.ivan.test.ecommerce.domain.model.Product;
import com.ivan.test.ecommerce.domain.model.ProductSize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class GetProductsWithStockTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GetProductsWithStock getProductsWithStock;

    @Test
    void should_throws_an_exception_given_not_params_when_product_repository_throws_an_exception() {
        //GIVEN
        assertThat(getProductsWithStock).isNotNull();
        given(productRepository.getProducts()).willThrow(new RuntimeException("UNIT TEST"));

        //WHEN
        final Throwable throwable = catchThrowable(() -> getProductsWithStock.get());

        //THEN
        assertThat(throwable).isNotNull()
            .isInstanceOf(EcommerceException.class);
        then(productRepository).should().getProducts();
    }

    @Test
    void should_return_empty_list_given_not_params_when_product_repository_returns_empty_list() {
        //GIVEN
        assertThat(getProductsWithStock).isNotNull();
        given(productRepository.getProducts()).willReturn(Collections.emptyList());

        //WHEN
        final List<Integer> response = getProductsWithStock.get();

        //THEN
        assertThat(response).isNotNull()
            .asList().hasSize(0);
        then(productRepository).should().getProducts();
    }

    @Test
    void should_return_empty_list_given_not_params_when_product_repository_returns_products_without_sizes() {
        //GIVEN
        assertThat(getProductsWithStock).isNotNull();
        final List<Product> storedProductsWithoutSizes = List.of(
            Product.builder().productId(1).position(0).build(),
            Product.builder().productId(2).position(1).build());

        given(productRepository.getProducts()).willReturn(storedProductsWithoutSizes);

        //WHEN
        final List<Integer> response = getProductsWithStock.get();

        //THEN
        assertThat(response).isNotNull()
            .asList().hasSize(0);
        then(productRepository).should().getProducts();
    }

    @Test
    void should_return_empty_list_given_not_params_when_product_repository_returns_products_without_stock() {
        //GIVEN
        assertThat(getProductsWithStock).isNotNull();
        final List<Product> storedProductsWithoutStock = List.of(
                Product.builder().productId(1).position(0).sizes(List.of(mockProductSizeWithoutStock(1))).build(),
                Product.builder().productId(2).position(1).sizes(List.of(mockProductSizeWithoutStock(2))).build());

        given(productRepository.getProducts()).willReturn(storedProductsWithoutStock);

        //WHEN
        final List<Integer> response = getProductsWithStock.get();

        //THEN
        assertThat(response).isNotNull()
            .asList().hasSize(0);
        then(productRepository).should().getProducts();
    }

    @Test
    void should_return_a_list_given_not_params_when_product_repository_returns_any_product_with_stock_then_the_response_has_the_product() {
        //GIVEN
        assertThat(getProductsWithStock).isNotNull();
        final List<Product> storedProductsWithStockProduct = List.of(
                Product.builder().productId(1).position(0).sizes(List.of(mockProductSizeWithoutStock(1))).build(),
                Product.builder().productId(2).position(1).sizes(List.of(mockProductSizeWithStock(2))).build());

        given(productRepository.getProducts()).willReturn(storedProductsWithStockProduct);

        //WHEN
        final List<Integer> response = getProductsWithStock.get();

        //THEN
        assertThat(response).isNotNull()
            .asList().hasSize(1)
            .first().isNotNull().isEqualTo(2);
        then(productRepository).should().getProducts();
    }

    @Test
    void should_return_a_list_given_not_params_when_product_repository_returns_any_back_soon_product_then_the_response_has_the_product() {
        //GIVEN
        assertThat(getProductsWithStock).isNotNull();
        final List<Product> storedProductsWithBackSoonProduct = List.of(
                Product.builder().productId(1).position(0).sizes(List.of(mockProductSizeWithoutStock(1))).build(),
                Product.builder().productId(2).position(1).sizes(List.of(mockBackSoonProductSize(2))).build());

        given(productRepository.getProducts()).willReturn(storedProductsWithBackSoonProduct);

        //WHEN
        final List<Integer> response = getProductsWithStock.get();

        //THEN
        assertThat(response).isNotNull()
                .asList().hasSize(1)
                .first().isNotNull().isEqualTo(2);
        then(productRepository).should().getProducts();
    }

    @Test
    void should_return_empty_list_given_not_params_when_product_repository_returns_an_special_size_and_there_are_not_any_other_available_size() {
        //GIVEN
        assertThat(getProductsWithStock).isNotNull();
        final List<Product> storedProductsWithSpecialProduct = List.of(
                Product.builder().productId(1).position(0).sizes(List.of(mockProductSizeWithoutStock(1))).build(),
                Product.builder().productId(2).position(1).sizes(List.of(mockSpecialProductSize(2))).build());

        given(productRepository.getProducts()).willReturn(storedProductsWithSpecialProduct);

        //WHEN
        final List<Integer> response = getProductsWithStock.get();

        //THEN
        assertThat(response).isNotNull()
                .asList().hasSize(0);
        then(productRepository).should().getProducts();
    }

    @Test
    void should_return_a_list_given_not_params_when_product_repository_returns_an_special_size_and_another_size_is_available_then_the_list_has_the_product() {
        //GIVEN
        assertThat(getProductsWithStock).isNotNull();
        final List<Product> storedProductsWithSpecialProduct = List.of(
                Product.builder().productId(1).position(0).sizes(List.of(mockProductSizeWithoutStock(1))).build(),
                Product.builder().productId(2).position(1).sizes(List.of(mockSpecialProductSize(2), mockBackSoonProductSize(3))).build());

        given(productRepository.getProducts()).willReturn(storedProductsWithSpecialProduct);

        //WHEN
        final List<Integer> response = getProductsWithStock.get();

        //THEN
        assertThat(response).isNotNull()
                .asList().hasSize(1)
                .first().isNotNull().isEqualTo(2);
        then(productRepository).should().getProducts();
    }
    @Test
    void should_return_sorted_list_given_not_params_when_product_repository_returns_not_shorted_products_with_stock() {
        //GIVEN
        assertThat(getProductsWithStock).isNotNull();
        final List<Product> storedProductsWithStock = List.of(
                Product.builder().productId(1).position(1).sizes(List.of(mockProductSizeWithStock(1))).build(),
                Product.builder().productId(2).position(0).sizes(List.of(mockProductSizeWithStock(2))).build());

        given(productRepository.getProducts()).willReturn(storedProductsWithStock);

        //WHEN
        final List<Integer> response = getProductsWithStock.get();

        //THEN
        assertThat(response).isNotNull().asList().hasSize(2);
        assertThat(response.get(0)).isNotNull().isEqualTo(2);
        assertThat(response.get(1)).isNotNull().isEqualTo(1);
        then(productRepository).should().getProducts();
    }

    private static ProductSize mockProductSizeWithoutStock(int sizeId) {
        return ProductSize.builder()
                .sizeId(sizeId)
                .backSoon(false)
                .special(false)
                .quantity(0)
                .build();
    }

    private static ProductSize mockProductSizeWithStock(int sizeId) {
        return ProductSize.builder()
                .sizeId(sizeId)
                .backSoon(false)
                .special(false)
                .quantity(10)
                .build();
    }

    private static ProductSize mockBackSoonProductSize(int sizeId) {
        return ProductSize.builder()
                .sizeId(sizeId)
                .backSoon(true)
                .special(false)
                .quantity(0)
                .build();
    }

    private static ProductSize mockSpecialProductSize(int sizeId) {
        return ProductSize.builder()
                .sizeId(sizeId)
                .backSoon(false)
                .special(true)
                .quantity(10)
                .build();
    }

}
