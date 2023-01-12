package com.ivan.test.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.mongodb.embedded.version=3.4.24"})
class EcommerceApplicationTests {

	@Test
	void contextLoads() {
	}

}
