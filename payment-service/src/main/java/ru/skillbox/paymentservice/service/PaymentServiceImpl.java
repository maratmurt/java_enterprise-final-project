package ru.skillbox.paymentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    public PaymentDto recharge(PaymentDto paymentDto, String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow();
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(paymentDto.getAmount());
        transaction.setDescription("Recharge payment");
        transactionRepository.save(transaction);
        log.info("Recharge payment: {}", paymentDto);
        paymentDto.setAccountId(account.getId());

        return paymentDto;
    }

}
