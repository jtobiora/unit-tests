package com.swiftfingers.integrationtests;



import com.swiftfingers.integrationtests.entity.Product;
		import org.junit.jupiter.api.BeforeAll;
		import org.junit.jupiter.api.BeforeEach;
		import org.junit.jupiter.api.Test;
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.boot.test.context.SpringBootTest;
		import org.springframework.boot.test.web.server.LocalServerPort;
		import org.springframework.test.annotation.DirtiesContext;
		import org.springframework.web.client.RestTemplate;

		import java.util.List;

		import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTestsApplicationTests {

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

	private void addProductToDatabase(Product product) {
		h2Repository.save(product);
	}

	private void deleteProductFromDatabase(int id) {
		h2Repository.deleteById(id);
	}

	/*
	* Note the use of @DirtiesContext annotation to indicate that the test method modifies the
	* application context and may affect the state of other tests. This ensures that the context
	* is reloaded after each test method.
	 * */
	@Test
	@DirtiesContext
	public void testAddProduct() {
		Product product = new Product("headset", 2, 7999);
		Product response = restTemplate.postForObject(baseUrl, product, Product.class);
		assertEquals("headset", response.getName());
		assertEquals(1, h2Repository.findAll().size());
	}

	@Test
	@DirtiesContext
	public void testGetProducts() {
		addProductToDatabase(new Product(4, "AC", 1, 34000));
		List<Product> products = restTemplate.getForObject(baseUrl, List.class);
		assertEquals(1, products.size());
		assertEquals(1, h2Repository.findAll().size());
		deleteProductFromDatabase(4);
	}

	@Test
	/*
	 * Note the use of @DirtiesContext annotation to indicate that the test method modifies the
	 * application context and may affect the state of other tests. This ensures that the context
	 * is reloaded after each test method.
	 * */
	@DirtiesContext
	public void testFindProductById() {
		addProductToDatabase(new Product(1, "CAR", 1, 334000));
		Product product = restTemplate.getForObject(baseUrl + "/{id}", Product.class, 1);
		assertAll(
				() -> assertNotNull(product),
				() -> assertEquals(1, product.getId()),
				() -> assertEquals("CAR", product.getName())
		);
		deleteProductFromDatabase(1);
	}

	@Test
	/*
	 * Note the use of @DirtiesContext annotation to indicate that the test method modifies the
	 * application context and may affect the state of other tests. This ensures that the context
	 * is reloaded after each test method.
	 * */
	@DirtiesContext
	public void testUpdateProduct() {
		addProductToDatabase(new Product(2, "shoes", 1, 999));
		Product product = new Product("shoes", 1, 1999);
		restTemplate.put(baseUrl + "/update/{id}", product, 2);
		Product productFromDB = h2Repository.findById(2).get();
		assertAll(
				() -> assertNotNull(productFromDB),
				() -> assertEquals(1999, productFromDB.getPrice())
		);
		deleteProductFromDatabase(2);
	}

	@Test
	@DirtiesContext
	public void testDeleteProduct() {
		addProductToDatabase(new Product(8, "books", 5, 1499));
		int recordCount = h2Repository.findAll().size();
		assertEquals(1, recordCount);
		restTemplate.delete(baseUrl + "/delete/{id}", 8);
		assertEquals(0, h2Repository.findAll().size());
	}
}
