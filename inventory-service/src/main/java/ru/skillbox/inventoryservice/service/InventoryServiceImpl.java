package ru.skillbox.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.inventoryservice.domain.*;
import ru.skillbox.inventoryservice.repository.InventoryRepository;
import ru.skillbox.inventoryservice.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    private final ProductRepository productRepository;

    @Override
    public List<Inventory> receipt(InventoryListDto inventoryListDto) {
        List<Inventory> inventoryList = new ArrayList<>();
        for (InventoryDto inventoryDto : inventoryListDto.getInventory()) {
            Inventory inventory = new Inventory();
            Product product = productRepository.findById(inventoryDto.getProductId()).orElseThrow();
            inventory.setProduct(product);
            inventory.setQuantity(inventoryDto.getQuantity());
            inventory.setDescription(inventoryListDto.getDescription());
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
    public boolean inventOrder(InventoryListDto inventoryListDto) {
        List<Inventory> inventoryList = new ArrayList<>();
        for (InventoryDto inventoryDto : inventoryListDto.getInventory()) {
            Product product = productRepository.findById(inventoryDto.getProductId()).orElseThrow();
            if (product.getQuantity() < inventoryDto.getQuantity()) {
                return false;
            }
            Inventory inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setQuantity(-inventoryDto.getQuantity());
            inventory.setDescription(inventoryListDto.getDescription());
            inventoryList.add(inventory);
        }
        inventoryRepository.saveAll(inventoryList);
        return true;
    }
}
