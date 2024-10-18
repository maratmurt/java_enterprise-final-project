package ru.skillbox.paymentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.orderservice.domain.OrderDto;
import ru.skillbox.paymentservice.domain.Account;
import ru.skillbox.paymentservice.domain.PaymentDto;
import ru.skillbox.paymentservice.domain.Transaction;
import ru.skillbox.paymentservice.repository.AccountRepository;
import ru.skillbox.paymentservice.repository.TransactionRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;

    @Override
    public Transaction recharge(PaymentDto paymentDto, String username) {
        Account account = accountRepository.findByUsername(username).orElseGet(() -> {
            Account newAccount = new Account();
            newAccount.setUsername(username);
            return accountRepository.save(newAccount);
        });
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(paymentDto.getAmount());
        transaction.setDescription("Recharge payment");
        transaction = transactionRepository.save(transaction);
        log.info("Recharge payment: {}", transaction);

        return transaction;
    }

    @Override
    public boolean payForOrder(OrderDto orderDto, String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow();
        if (account.getBalance() < orderDto.getCost()) {
            log.info("Payment failed: not enough money");
            return false;
        }

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(-orderDto.getCost());
        transaction.setDescription("Payment for order");
        transaction = transactionRepository.save(transaction);
        log.info("Payment for order: {}", transaction);

        return true;
    }

    @Override
    public void refundOrder(String username, Long orderId, Double amount) {
        Account account = accountRepository.findByUsername(username).orElseThrow();
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setDescription("Order " + orderId + " refund");
        transactionRepository.save(transaction);
    }

}
