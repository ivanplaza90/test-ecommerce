package com.ivan.test.ecommerce.application;

import com.ivan.test.ecommerce.domain.ProductRepository;
import com.ivan.test.ecommerce.domain.exception.EcommerceException;
import com.ivan.test.ecommerce.domain.exception.ProductNotFoundException;
import com.ivan.test.ecommerce.domain.exception.ProductWithoutSizesException;
import com.ivan.test.ecommerce.domain.exception.ProductWithoutStockException;
import com.ivan.test.ecommerce.domain.model.Product;
import com.ivan.test.ecommerce.domain.model.ProductSize;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Slf4j
public class GetProductWithStock implements Function<Integer, Product> {


    private static final Predicate<ProductSize> SIZE_WITH_STOCK_OR_BACK_SOON_OR_SPECIAL =
            size -> size.getQuantity() > 0 || size.isBackSoon() || size.isSpecial();
    private ProductRepository productRepository;
    @Override
    public Product apply(final Integer productId) {
        return getStoredProduct(productId)
            .map(product -> {
                Optional.ofNullable(product.getSizes())
                        .orElseThrow(() -> new ProductWithoutSizesException(String.format("The product with Id %d does not have sizes", product.getProductId())));
                return product;
            })
            .map(product -> product.withSizes(getSizesWithStock(product.getSizes())))
            .orElseThrow(() -> new ProductNotFoundException(String.format("The product with Id %d not found", productId)));
    }

    private Optional<Product> getStoredProduct(final Integer productId) {
        try{
            return productRepository.getProduct(productId);
        } catch(Exception e){
            log.warn("Error while calling product repository");
            throw new EcommerceException("An error occur while try to get products from product repository");
        }
    }

    private List<ProductSize> getSizesWithStock(final List<ProductSize> sizes) {
        final List<ProductSize> processedSizes = sizes.stream()
                .filter(SIZE_WITH_STOCK_OR_BACK_SOON_OR_SPECIAL)
                .collect(Collectors.toList());

        if(processedSizes.size() == 0)
            throw new ProductWithoutStockException("The product has not any size available");
        if(processedSizes.size() == 1)
            if(processedSizes.get(0).isSpecial()) {
                throw new ProductWithoutStockException("The product has not any size available");
            }

        return processedSizes;
    }
}
