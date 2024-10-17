package ru.skillbox.inventoryservice.service;

import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.domain.InventoryListDto;
import ru.skillbox.inventoryservice.domain.Product;
import ru.skillbox.inventoryservice.domain.ProductDto;

import java.util.List;

public interface InventoryService {

    List<Inventory> receipt(InventoryListDto inventoryListDto);

    Product addProduct(ProductDto productDto);

    boolean inventOrder(InventoryListDto inventoryListDto);

}
