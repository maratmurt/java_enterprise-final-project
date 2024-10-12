package ru.skillbox.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.paymentservice.domain.UserBalance;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {
}
