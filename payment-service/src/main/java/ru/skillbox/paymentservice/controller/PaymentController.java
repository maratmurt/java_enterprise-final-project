package ru.skillbox.paymentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.paymentservice.domain.PaymentDto;
import ru.skillbox.paymentservice.domain.Transaction;
import ru.skillbox.paymentservice.service.PaymentService;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Top up the clients balance", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/recharge")
    public ResponseEntity<Transaction> recharge(@RequestBody PaymentDto paymentDto,
                                                @RequestHeader(name = "X-Username") String username) {
        return ResponseEntity.ok(paymentService.recharge(paymentDto, username));
    }

}
