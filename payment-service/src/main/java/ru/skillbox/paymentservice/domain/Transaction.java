package ru.skillbox.paymentservice.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "account_id")
    @ManyToOne
    private Account account;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "description")
    private String description;

}
