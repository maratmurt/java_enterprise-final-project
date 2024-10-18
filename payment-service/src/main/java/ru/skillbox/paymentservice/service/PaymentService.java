package ru.skillbox.paymentservice.service;

import ru.skillbox.orderservice.domain.OrderDto;
import ru.skillbox.paymentservice.domain.PaymentDto;
import ru.skillbox.paymentservice.domain.Transaction;

public interface PaymentService {

    Transaction recharge(PaymentDto paymentDto, String username);

    boolean payForOrder(OrderDto orderDto, String username);

    void refundOrder(String username, Long orderId, Double amount);

}
