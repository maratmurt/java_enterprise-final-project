package ru.skillbox.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.paymentservice.domain.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

}
