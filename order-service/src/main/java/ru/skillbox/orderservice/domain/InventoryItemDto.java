package ru.skillbox.orderservice.domain;

import lombok.Data;

@Data
public class InventoryItemDto {

    private Long productId;

    private Integer quantity;

}
