package ru.skillbox.inventoryservice.service;

import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.domain.Product;
import ru.skillbox.inventoryservice.domain.ProductDto;
import ru.skillbox.orderservice.domain.InventoryItemDto;
import ru.skillbox.orderservice.domain.OrderDto;

import java.util.List;

public interface InventoryService {

    List<Inventory> receipt(List<InventoryItemDto> items, String description);

    Product addProduct(ProductDto productDto);

    boolean inventOrder(OrderDto orderDto);

}
