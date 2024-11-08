package ru.skillbox.inventoryservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    private Integer quantity;

    private String description;

}
