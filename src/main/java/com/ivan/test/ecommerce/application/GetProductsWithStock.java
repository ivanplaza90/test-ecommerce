package com.ivan.test.ecommerce.application;

import com.ivan.test.ecommerce.domain.ProductRepository;
import com.ivan.test.ecommerce.domain.exception.EcommerceException;
import com.ivan.test.ecommerce.domain.model.Product;
import com.ivan.test.ecommerce.domain.model.ProductSize;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class GetProductsWithStock {

    private static final Predicate<Product> PRODUCT_WITH_SIZES =
        product -> product.getSizes() != null && product.getSizes().size() > 0;
    private static final Predicate<ProductSize> SIZE_WITH_STOCK_OR_BACK_SOON_OR_SPECIAL =
            size -> size.getQuantity() > 0 || size.isBackSoon() || size.isSpecial();

    private ProductRepository productRepository;
    public List<Product> get() {
        return getStoredProducts().stream()
                .filter(PRODUCT_WITH_SIZES)
                .filter(product -> getSizesWithStock(product.getSizes()).size() > 0)
                .sorted(Comparator.comparing(Product::getPosition))
                .collect(Collectors.toList());
    }

    private List<Product> getStoredProducts() {
        try{
            return productRepository.getProducts();
        } catch(Exception e){
            log.warn("Error while calling product repository");
            throw new EcommerceException("An error occur while try to get products from product repository");
        }
    }

    private List<ProductSize> getSizesWithStock(final List<ProductSize> sizes) {
        final List<ProductSize> processedProducts = sizes.stream()
                .filter(SIZE_WITH_STOCK_OR_BACK_SOON_OR_SPECIAL)
                .collect(Collectors.toList());
        if(processedProducts.size()==1 && processedProducts.get(0).isSpecial())
            return Collections.emptyList();
        return processedProducts;
    }
}
