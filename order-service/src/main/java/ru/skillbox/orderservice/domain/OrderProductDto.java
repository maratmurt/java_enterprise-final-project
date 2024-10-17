package ru.skillbox.orderservice.domain;

import lombok.Data;

@Data
public class OrderProductDto {

    private Long productId;

    private Integer quantity;

}
