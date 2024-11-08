package ru.skillbox.paymentservice.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "account_id")
    @ManyToOne
    private Account account;

    private Double amount;

    private String description;

}
