package com.swiftfingers.integrationtests;


import com.swiftfingers.integrationtests.entity.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootIntegrationTests2 {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private TestH2Repository h2Repository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/products");
    }

    // Helper method to add product to the database
    //In this approach, each test method calls a helper method (addProductToDatabase) to add
    // data to the database before performing the actual test. This way, you achieve similar
    // setup without using @Sql.
    private void addProductToDatabase(String name, int quantity, int price) {
        Product product = new Product(name, quantity, price);
        restTemplate.postForObject(baseUrl, product, Product.class);
    }

    @Test
    public void testAddProduct() {
        addProductToDatabase("headset", 2, 7999);
        assertEquals(1, h2Repository.findAll().size());
    }

    @Test
    public void testGetProducts() {
        addProductToDatabase("AC", 1, 34000);

        List<Product> products = restTemplate.getForObject(baseUrl, List.class);
        assertEquals(1, products.size());
        assertEquals(1, h2Repository.findAll().size());
    }

    @Test
    public void testFindProductById() {
        addProductToDatabase("CAR", 1, 334000);

        Product product = restTemplate.getForObject(baseUrl + "/{id}", Product.class, 1);
        assertAll(
                () -> assertNotNull(product),
                () -> assertEquals(1, product.getId()),
                () -> assertEquals("CAR", product.getName())
        );
    }

    @Test
    public void testUpdateProduct() {
        addProductToDatabase("shoes", 1, 999);

        Product product = new Product("shoes", 1, 1999);
        restTemplate.put(baseUrl + "/update/{id}", product, 2);

        Product productFromDB = h2Repository.findById(2).get();
        assertAll(
                () -> assertNotNull(productFromDB),
                () -> assertEquals(1999, productFromDB.getPrice())
        );
    }

    @Test
    public void testDeleteProduct() {
        addProductToDatabase("books", 5, 1499);
        int recordCount = h2Repository.findAll().size();
        assertEquals(1, recordCount);

        restTemplate.delete(baseUrl + "/delete/{id}", 8);
        assertEquals(0, h2Repository.findAll().size());
    }
}
