package com.ivan.test.ecommerce.application;

import com.ivan.test.ecommerce.domain.model.Product;
import com.ivan.test.ecommerce.infrastructure.data.mongo.ProductMongoRepository;
import com.ivan.test.ecommerce.infrastructure.data.mongo.SizeMongoRepository;
import com.ivan.test.ecommerce.infrastructure.data.mongo.StockMongoRepository;
import com.ivan.test.ecommerce.infrastructure.data.mongo.model.ProductEntity;
import com.ivan.test.ecommerce.infrastructure.data.mongo.model.SizeEntity;
import com.ivan.test.ecommerce.infrastructure.data.mongo.model.StockEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = {"spring.mongodb.embedded.version=3.4.24"})
@ExtendWith(SpringExtension.class)
class GetProductsWithStockIT {

    @Autowired
    private ProductMongoRepository productMongoRepository;
    @Autowired
    private SizeMongoRepository sizeMongoRepository;
    @Autowired
    private StockMongoRepository stockMongoRepository;
    
    @Autowired
    private GetProductsWithStock getProductsWithStock;


    @Test
    void should_get_a_list_of_products_given_not_params_when_the_database_has_data() throws IOException {
        //GIVEN
        productMongoRepository.saveAll(loadProductsFromFile());
        sizeMongoRepository.saveAll(loadSizesFromFile());
        stockMongoRepository.saveAll(loadStockFromFile());

        //WHEN
        final List<Integer> products = getProductsWithStock.get();

        //THEN
        assertThat(products).isNotNull().asList().hasSize(3);
        assertThat(products.get(0)).isNotNull().isEqualTo(5);
        assertThat(products.get(1)).isNotNull().isEqualTo(1);
        assertThat(products.get(2)).isNotNull().isEqualTo(3);
    }

    private List<StockEntity> loadStockFromFile() throws IOException {
        ArrayList<StockEntity> loadedStocks = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/data/stock.csv")));
        String fileLine = bufferedReader.readLine();

        while(fileLine != null) {
            String[] attributes = fileLine.split(",");
            loadedStocks.add(StockEntity.builder()
                .sizeId(Integer.parseInt(attributes[0].trim()))
                .quantity(Integer.parseInt(attributes[1].trim()))
                .build());
            fileLine = bufferedReader.readLine();
        }
        bufferedReader.close();

        return loadedStocks;
    }

    private List<SizeEntity> loadSizesFromFile() throws IOException {
        ArrayList<SizeEntity> loadedSizes = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/data/size.csv")));
        String fileLine = bufferedReader.readLine();

        while(fileLine != null) {
            String[] attributes = fileLine.split(",");
            loadedSizes.add(SizeEntity.builder()
                    .sizeId(Integer.parseInt(attributes[0].trim()))
                    .productId(Integer.parseInt(attributes[1].trim()))
                    .backSoon(Boolean.parseBoolean(attributes[2].trim()))
                    .special(Boolean.parseBoolean(attributes[3].trim()))
                    .build());
            fileLine = bufferedReader.readLine();
        }
        bufferedReader.close();

        return loadedSizes;
    }

    private List<ProductEntity> loadProductsFromFile() throws IOException {
        ArrayList<ProductEntity> loadedProduct = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/data/product.csv")));
        String fileLine = bufferedReader.readLine();

        while(fileLine != null) {
            String[] attributes = fileLine.split(",");
            loadedProduct.add(ProductEntity.builder()
                    .productId(Integer.parseInt(attributes[0].trim()))
                    .position(Integer.parseInt(attributes[1].trim()))
                    .build());
            fileLine = bufferedReader.readLine();
        }
        bufferedReader.close();

        return loadedProduct;
    }
}
