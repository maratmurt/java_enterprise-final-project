package ru.skillbox.inventoryservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Transient
    private Integer quantity;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Inventory> inventoryHistory = new ArrayList<>();

    public Integer getQuantity() {
        return inventoryHistory.stream()
                .map(Inventory::getQuantity)
                .reduce(0, Integer::sum);
    }
}
