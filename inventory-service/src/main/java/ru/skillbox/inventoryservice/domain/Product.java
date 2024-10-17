package ru.skillbox.inventoryservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @Transient
    private Integer quantity = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Inventory> inventoryHistory = new ArrayList<>();

    public Integer getQuantity() {
        return inventoryHistory.stream()
                .map(Inventory::getQuantity)
                .reduce(0, Integer::sum);
    }
}
