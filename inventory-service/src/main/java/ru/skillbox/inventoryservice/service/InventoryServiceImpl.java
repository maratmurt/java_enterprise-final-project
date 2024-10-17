package ru.skillbox.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.inventoryservice.domain.*;
import ru.skillbox.inventoryservice.repository.InventoryRepository;
import ru.skillbox.inventoryservice.repository.ProductRepository;
import ru.skillbox.orderservice.domain.InventoryItemDto;
import ru.skillbox.orderservice.domain.OrderDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    private final ProductRepository productRepository;

    @Override
    public List<Inventory> receipt(List<InventoryItemDto> items) {
        List<Inventory> inventoryList = new ArrayList<>();
        for (InventoryItemDto inventoryItemDto : items) {
            Inventory inventory = new Inventory();
            Product product = productRepository.findById(inventoryItemDto.getProductId()).orElseThrow();
            inventory.setProduct(product);
            inventory.setQuantity(inventoryItemDto.getQuantity());
            inventory.setDescription("Goods receipt");
            inventoryList.add(inventory);
        }
        return inventoryRepository.saveAll(inventoryList);
    }

    @Override
    public Product addProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        return productRepository.save(product);
    }

    @Override
    public boolean inventOrder(OrderDto orderDto) {
        List<Inventory> inventoryList = new ArrayList<>();
        for (InventoryItemDto inventoryItemDto : orderDto.getInventory()) {
            Product product = productRepository.findById(inventoryItemDto.getProductId()).orElseThrow();
            if (product.getQuantity() < inventoryItemDto.getQuantity()) {
                return false;
            }
            Inventory inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setQuantity(-inventoryItemDto.getQuantity());
            inventory.setDescription(orderDto.getDescription());
            inventoryList.add(inventory);
        }
        inventoryRepository.saveAll(inventoryList);
        return true;
    }
}
