package ru.skillbox.deliveryservice.domain;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.orderservice.domain.DeliveryStatus;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "departure_address")
    private String departureAddress;

    @Column(name = "destination_address")
    private String destinationAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

}
