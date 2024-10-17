package ru.skillbox.inventoryservice.domain;

import lombok.Data;

@Data
public class InventoryDto {

    private Long productId;

    private Integer quantity;

}
