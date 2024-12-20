package ru.skillbox.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.domain.OrderDto;
import ru.skillbox.orderservice.domain.StatusDto;
import ru.skillbox.orderservice.repository.OrderRepository;
import ru.skillbox.orderservice.service.OrderService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;

    private final OrderService orderService;

    @Operation(summary = "List all orders in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/")
    public List<Order> listOrders() {
        return orderRepository.findAll();
    }

    @Operation(summary = "Get an order in system by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{orderId}")
    public Order listOrder(@PathVariable @Parameter(description = "Id of order") Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Operation(summary = "Add order and start delivery process for it", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/")
    public ResponseEntity<Order> addOrder(@RequestBody OrderDto input,
                                      @RequestHeader(name = "X-Username", required = false) String username) {
        return orderService.addOrder(input, username)
                .map(order -> ResponseEntity.status(HttpStatus.CREATED).body(order))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Update order status", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable @Parameter(description = "Id of order") long orderId,
                                               @RequestBody StatusDto statusDto) {
        log.info("ORDER {} UPDATE: {}", orderId, statusDto);
        try {
            orderService.updateOrderStatus(orderId, statusDto);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Can't change status for order with id {}", orderId, ex);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }
}
