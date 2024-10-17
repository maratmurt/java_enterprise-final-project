package ru.skillbox.inventoryservice.domain;

import lombok.Data;

import java.util.List;

@Data
public class InventoryListDto {

    private List<InventoryDto> inventory;

    private String description;

}
