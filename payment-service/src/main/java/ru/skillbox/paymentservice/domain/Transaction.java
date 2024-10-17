package ru.skillbox.paymentservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "account_id")
    @ManyToOne
    private Account account;

    private Double amount;

    private String description;

}
