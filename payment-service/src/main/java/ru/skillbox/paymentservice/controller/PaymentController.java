package ru.skillbox.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.paymentservice.domain.PaymentDto;
import ru.skillbox.paymentservice.service.PaymentService;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/recharge")
    public ResponseEntity<PaymentDto> recharge(@RequestBody PaymentDto paymentDto,
                                               @RequestHeader(name = "X-Username", required = false) String username) {
        return ResponseEntity.ok(paymentService.recharge(paymentDto, username));
    }

}
