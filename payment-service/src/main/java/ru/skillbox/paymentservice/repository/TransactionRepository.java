package ru.skillbox.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.paymentservice.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
