package ru.skillbox.inventoryservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@EnableTestBinder
@ActiveProfiles("test")
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InventoryServiceAppTest {

    @LocalServerPort
    protected Integer port;

    @Autowired
    protected InputDestination input;

    @Autowired
    protected OutputDestination output;

    @Autowired
    protected RestTemplate restTemplate;

    protected static final KafkaContainer KAFKA;

    protected static final PostgreSQLContainer<?> POSTGRES;

    static {
        KAFKA = new KafkaContainer(
                DockerImageName.parse("confluentinc/cp-kafka")
        );
        POSTGRES = new PostgreSQLContainer<>(
                DockerImageName.parse("postgres")
        );
        KAFKA.start();
        POSTGRES.start();
    }

    @DynamicPropertySource
    protected static void configureDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
    }

}
