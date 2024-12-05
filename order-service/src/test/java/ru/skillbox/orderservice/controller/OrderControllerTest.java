package ru.skillbox.orderservice.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.domain.OrderDto;
import ru.skillbox.orderservice.domain.OrderStatus;
import ru.skillbox.orderservice.domain.StatusDto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestChannelBinderConfiguration.class)
public class OrderControllerTest {

    @LocalServerPort
    private Integer port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OutputDestination streamOutput;

    @Container
    private static final KafkaContainer KAFKA = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka")
    );

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres");

    @DynamicPropertySource
    public static void configureDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
    }

    @BeforeAll
    static void beforeAll() {
        POSTGRES.start();
        KAFKA.start();
    }

    @AfterAll
    static void afterAll() {
        POSTGRES.stop();
        KAFKA.stop();
    }

    @Test
    public void listOrdersTest() {
        ResponseEntity<List<Order>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/order/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){}
        );
        List<Order> orders = Objects.requireNonNull(response.getBody());

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(orders.size()).isEqualTo(2);
    }

    @Test
    public void listOrderTest() {
        ResponseEntity<Order> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/order/1",
                Order.class
        );
        Order actualOrder = Objects.requireNonNull(response.getBody());

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(actualOrder.getId()).isEqualTo(1);
    }

    @Test
    public void updateOrderStatusTest() {
        HttpEntity<StatusDto> request = new HttpEntity<>(new StatusDto(OrderStatus.PAID));
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/order/1",
                HttpMethod.PATCH,
                request,
                Void.class
        );

        Order order = jdbcTemplate.query(
                "SELECT * FROM orders WHERE id = 1",
                new BeanPropertyRowMapper<>(Order.class)
        ).get(0);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(order).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    public void addOrderTest() {
        OrderDto orderDto = new OrderDto(
                "New order description",
                "Departure address",
                "Destination address",
                300D,
                Collections.emptyList()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Username", "User 1");
        HttpEntity<OrderDto> request = new HttpEntity<>(orderDto, headers);

        ResponseEntity<Order> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/order/",
                HttpMethod.POST,
                request,
                Order.class
        );
        int orderCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders");
        Message<byte[]> outputMessage = streamOutput.receive(100, "order");

        assertThat(outputMessage).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(orderCount).isEqualTo(3);
    }

}
