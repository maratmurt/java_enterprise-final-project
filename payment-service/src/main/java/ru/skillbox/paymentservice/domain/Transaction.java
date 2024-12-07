package ru.skillbox.paymentservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "description")
    private String description;

    public Transaction(
            Account account,
            Double amount,
            String description
    ) {
        this.account = account;
        this.amount = amount;
        this.description = description;
    }
}
