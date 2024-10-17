package ru.skillbox.inventoryservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.domain.InventoryListDto;
import ru.skillbox.inventoryservice.domain.Product;
import ru.skillbox.inventoryservice.domain.ProductDto;
import ru.skillbox.inventoryservice.service.InventoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/inventory")
    public ResponseEntity<List<Inventory>> receipt(@RequestBody InventoryListDto inventoryListDto) {
        return ResponseEntity.ok(inventoryService.receipt(inventoryListDto));
    }

    @PostMapping("/product")
    public ResponseEntity<Product> addProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(inventoryService.addProduct(productDto));
    }

}
