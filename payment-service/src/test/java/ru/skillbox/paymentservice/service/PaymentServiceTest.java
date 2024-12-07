package ru.skillbox.paymentservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.orderservice.domain.OrderDto;
import ru.skillbox.paymentservice.domain.Account;
import ru.skillbox.paymentservice.domain.PaymentDto;
import ru.skillbox.paymentservice.domain.Transaction;
import ru.skillbox.paymentservice.repository.AccountRepository;
import ru.skillbox.paymentservice.repository.TransactionRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    public void setUp() {
        Account account = new Account();
        account.setId(1L);
        account.setUsername("User 1");

        Transaction balanceTransaction = new Transaction();
        balanceTransaction.setId(1L);
        balanceTransaction.setAmount(200D);
        balanceTransaction.setAccount(account);

        account.setTransactions(Collections.singletonList(balanceTransaction));

        when(accountRepository.findByUsername("User 1")).thenReturn(Optional.of(account));
    }

    @Test
    public void rechargeTest() {
        paymentService.recharge(new PaymentDto(100D), "User 1");

        verify(accountRepository).findByUsername("User 1");
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void payForOrderTest() {
        OrderDto orderDto = new OrderDto(
                "Order description",
                "Departure address",
                "Destination address",
                100D,
                Collections.emptyList()
        );

        assertTrue(paymentService.payForOrder(orderDto, "User 1"));
        verify(accountRepository).findByUsername("User 1");
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void refundOrderTest() {
        paymentService.refundOrder("User 1", 1L, 100D);

        verify(accountRepository).findByUsername("User 1");
        verify(transactionRepository).save(any(Transaction.class));
    }

}
