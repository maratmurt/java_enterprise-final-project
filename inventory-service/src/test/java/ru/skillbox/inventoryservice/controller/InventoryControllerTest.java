package ru.skillbox.inventoryservice.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.skillbox.inventoryservice.InventoryServiceAppTest;
import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.domain.Product;
import ru.skillbox.inventoryservice.domain.ProductDto;
import ru.skillbox.orderservice.domain.InventoryItemDto;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InventoryControllerTest extends InventoryServiceAppTest {

    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void afterEach() {
        jdbcTemplate.execute("TRUNCATE TABLE products CASCADE");
    }

    @Test
    public void testReceipt() {
        jdbcTemplate.execute(
                "INSERT INTO products (id, name) VALUES (1, 'Product 1')"
        );

        InventoryItemDto item = new InventoryItemDto();
        item.setQuantity(10);
        item.setProductId(1L);

        HttpEntity<List<InventoryItemDto>> request =
                new HttpEntity<>(Collections.singletonList(item));
        ResponseEntity<List<Inventory>> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/inventory/receipt",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {}
        );

        Integer actualProductQuantity = jdbcTemplate.queryForObject(
                "SELECT quantity FROM inventory WHERE product_id = 1",
                Integer.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertEquals(10, actualProductQuantity);
    }

    @Test
    public void testAddProduct() {
        ProductDto requestBody = new ProductDto();
        requestBody.setName("Product 1");
        ResponseEntity<Product> response = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/api/inventory/product",
                new HttpEntity<>(requestBody),
                Product.class
        );

        String actualProductName = jdbcTemplate.queryForObject(
                "SELECT name FROM products WHERE name = 'Product 1'",
                String.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Product 1", actualProductName);
    }

}
