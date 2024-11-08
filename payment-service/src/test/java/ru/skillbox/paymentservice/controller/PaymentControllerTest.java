package ru.skillbox.paymentservice.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.skillbox.paymentservice.domain.PaymentDto;
import ru.skillbox.paymentservice.domain.Transaction;
import ru.skillbox.paymentservice.repository.AccountRepository;
import ru.skillbox.paymentservice.repository.TransactionRepository;
import ru.skillbox.paymentservice.service.PaymentService;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentControllerTest {

    @LocalServerPort
    private Integer port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeAll
    public static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    public static void configurePostgreSQL(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    public void rechargeTest() {
        String url = "http://localhost:" + port + "/pay/recharge";
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Username", "User 1");
        HttpEntity<PaymentDto> request = new HttpEntity<>(new PaymentDto(100D), headers);
        ResponseEntity<Transaction> response = restTemplate.postForEntity(url, request, Transaction.class);

        double actualTransactionAmount = Objects.requireNonNull(response.getBody()).getAmount();
        double actualBalance = accountRepository.findByUsername("User 1")
                .orElseThrow()
                .getBalance();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(100D, actualTransactionAmount);
        assertEquals(100D, actualBalance);
    }

}
