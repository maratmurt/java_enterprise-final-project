package ru.skillbox.deliveryservice.handler;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import ru.skillbox.deliveryservice.repository.DeliveryRepository;
import ru.skillbox.event.InventoryEvent;
import ru.skillbox.orderservice.domain.InventoryStatus;
import ru.skillbox.orderservice.domain.OrderDto;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ActiveProfiles("test")
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
@Slf4j
@AutoConfigureMockMvc
public class InventoryEventHandlerTest {

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Autowired
    private OutputDestination output;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Container
    private static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @DynamicPropertySource
    static void registryProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    public static void beforeAll() {
        kafka.start();
        postgres.start();
    }

    @AfterAll
    public static void afterAll() {
        kafka.stop();
        postgres.stop();
    }

    @BeforeEach
    public void beforeEach() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void test() {
        mockServer.expect(requestTo("http://localhost:8080/api/order/1"))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        OrderDto order = new OrderDto(
                "Order description",
                "Departure address",
                "Destination address",
                100D,
                Collections.emptyList());
        InventoryEvent inventoryEvent = new InventoryEvent(
                1L,
                "User 1",
                InventoryStatus.COMPLETE.name(),
                order);
        streamBridge.send("inventory", inventoryEvent);

        Message<byte[]> outputMessage = output.receive(100, "delivery");

        assertThat(outputMessage).isNotNull();
        assertEquals(1, deliveryRepository.findAll().size());
    }

}
