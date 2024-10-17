package ru.skillbox.paymentservice.service;

import ru.skillbox.paymentservice.domain.PaymentDto;

public interface PaymentService {

    PaymentDto recharge(PaymentDto paymentDto, String username);

}
