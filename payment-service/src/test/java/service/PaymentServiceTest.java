package service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.skillbox.orderservice.domain.OrderDto;
import ru.skillbox.paymentservice.domain.Account;
import ru.skillbox.paymentservice.domain.PaymentDto;
import ru.skillbox.paymentservice.domain.Transaction;
import ru.skillbox.paymentservice.repository.AccountRepository;
import ru.skillbox.paymentservice.repository.TransactionRepository;
import ru.skillbox.paymentservice.service.PaymentService;
import ru.skillbox.paymentservice.service.PaymentServiceImpl;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentServiceTest {

    private final AccountRepository accountRepository = Mockito.mock(AccountRepository.class);

    private final TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);

    private final PaymentService paymentService = new PaymentServiceImpl(transactionRepository, accountRepository);

    @Test
    public void whenRecharge_thenReturnTransaction() {
        Account account = new Account();
        account.setUsername("User 1");

        Mockito.when(accountRepository.findByUsername("User 1")).thenReturn(Optional.of(account));

        Transaction inputTransaction = new Transaction();
        inputTransaction.setAmount(100D);
        inputTransaction.setAccount(account);
        inputTransaction.setDescription("Recharge payment");

        Transaction outputTransaction = new Transaction();
        outputTransaction.setId(1L);
        outputTransaction.setAmount(100D);
        outputTransaction.setAccount(account);
        outputTransaction.setDescription("Recharge payment");

        Mockito.when(transactionRepository.save(Mockito.eq(inputTransaction))).thenReturn(outputTransaction);

        Transaction actualTransaction = paymentService.recharge(new PaymentDto(100D), "User 1");

        assertEquals(outputTransaction, actualTransaction);
    }

    @Test
    public void whenPayForOrder_thenReturnTrue() {
        Account account = new Account();
        account.setUsername("User 1");
        Transaction balanceTransaction = new Transaction();
        balanceTransaction.setAmount(200D);
        balanceTransaction.setAccount(account);
        account.setTransactions(Collections.singletonList(balanceTransaction));

        Mockito.when(accountRepository.findByUsername("User 1")).thenReturn(Optional.of(account));

        OrderDto orderDto = new OrderDto(
                "Order description",
                "Departure address",
                "Destination address",
                100D,
                Collections.emptyList());

        Transaction outputTransaction = new Transaction();
        outputTransaction.setId(1L);
        outputTransaction.setAmount(-100D);
        outputTransaction.setAccount(account);
        outputTransaction.setDescription("Payment for order");

        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(outputTransaction);

        assertTrue(paymentService.payForOrder(orderDto, "User 1"));
    }

}
