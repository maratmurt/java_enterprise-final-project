package ru.skillbox.inventoryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.domain.Product;
import ru.skillbox.inventoryservice.domain.ProductDto;
import ru.skillbox.inventoryservice.service.InventoryService;
import ru.skillbox.orderservice.domain.InventoryItemDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @Operation(summary = "Process the list of received goods", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/receipt")
    public ResponseEntity<List<Inventory>> receipt(@RequestBody List<InventoryItemDto> items) {
        return ResponseEntity.ok(inventoryService.receipt(items, "Goods receipt"));
    }

    @Operation(summary = "Add new item to product list", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/product")
    public ResponseEntity<Product> addProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(inventoryService.addProduct(productDto));
    }

}
