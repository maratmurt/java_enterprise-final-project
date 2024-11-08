package ru.skillbox.paymentservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Transient
    private Double balance;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    List<Transaction> transactions = new ArrayList<>();

    public Double getBalance() {
        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(0.0, Double::sum);
    }
}
