package ru.skillbox.inventoryservice.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.domain.Product;
import ru.skillbox.inventoryservice.domain.ProductDto;
import ru.skillbox.inventoryservice.repository.InventoryRepository;
import ru.skillbox.inventoryservice.repository.ProductRepository;
import ru.skillbox.orderservice.domain.InventoryItemDto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InventoryControllerTest {

    @LocalServerPort
    private Integer port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    public static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }

    @AfterEach
    public void afterEach() {
        inventoryRepository.deleteAll();
        productRepository.deleteAll();
    }

    @DynamicPropertySource
    public static void configurePostgreSQL(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    public void testReceipt() {
        Product product = new Product();
        product.setName("Product 1");
        product.setDescription("Product 1 description");
        product = productRepository.save(product);

        InventoryItemDto item = new InventoryItemDto();
        item.setQuantity(1);
        item.setProductId(product.getId());

        String url = "http://localhost:" + port + "/inv/inventory";
        HttpEntity<List<InventoryItemDto>> request =
                new HttpEntity<>(Collections.singletonList(item));
        ResponseEntity<List<Inventory>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {});

        int expectedProductQuantity = productRepository.findById(product.getId())
                .orElseThrow()
                .getQuantity();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, expectedProductQuantity);
    }

    @Test
    public void testAddProduct() {
        String url = "http://localhost:" + port + "/inv/product";
        ProductDto requestBody = new ProductDto();
        requestBody.setName("Product 1");
        ResponseEntity<Product> response = restTemplate.postForEntity(url, new HttpEntity<>(requestBody), Product.class);

        Optional<Product> optional = productRepository.findById(
                Objects.requireNonNull(response.getBody()).getId());

        assertEquals(200, response.getStatusCode().value());
        assertTrue(optional.isPresent(), "Product not found");
    }

}
