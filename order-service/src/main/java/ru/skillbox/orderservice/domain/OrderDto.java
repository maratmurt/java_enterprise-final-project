package ru.skillbox.orderservice.domain;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {

    private String description;

    private String departureAddress;

    private String destinationAddress;

    private Double cost;

    private List<OrderProductDto> products;

}
