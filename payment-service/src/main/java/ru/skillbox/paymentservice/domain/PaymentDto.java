package ru.skillbox.paymentservice.domain;

import lombok.Data;

@Data
public class PaymentDto {

    private Double amount;

    private Long accountId;

}
