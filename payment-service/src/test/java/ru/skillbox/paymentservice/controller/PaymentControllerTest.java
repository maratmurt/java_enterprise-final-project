package ru.skillbox.paymentservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.skillbox.paymentservice.PaymentServiceAppTest;
import ru.skillbox.paymentservice.domain.PaymentDto;
import ru.skillbox.paymentservice.domain.Transaction;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentControllerTest extends PaymentServiceAppTest {

    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Test
    public void rechargeTest() {
        String url = "http://localhost:" + port + "/api/payment/recharge";
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Username", "User 1");
        HttpEntity<PaymentDto> request = new HttpEntity<>(new PaymentDto(100D), headers);

        ResponseEntity<Transaction> response = testRestTemplate.postForEntity(url, request, Transaction.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAmount()).isEqualTo(100D);
    }

}
