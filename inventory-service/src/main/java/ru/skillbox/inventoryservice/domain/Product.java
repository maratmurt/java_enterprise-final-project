package ru.skillbox.inventoryservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Inventory> inventoryHistory = new ArrayList<>();

    public Product(
            String name,
            String description,
            List<Inventory> inventoryHistory
    ) {
        this.name = name;
        this.description = description;
        this.inventoryHistory = inventoryHistory;
    }

    public Integer getQuantity() {
        return inventoryHistory.stream()
                .map(Inventory::getQuantity)
                .reduce(0, Integer::sum);
    }
}
