package ru.skillbox.deliveryservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.orderservice.domain.DeliveryStatus;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "departure_address")
    private String departureAddress;

    @Column(name = "destination_address")
    private String destinationAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeliveryStatus status;

    public Delivery(
            Long orderId,
            String departureAddress,
            String destinationAddress,
            DeliveryStatus status
    ) {
        this.orderId = orderId;
        this.departureAddress = departureAddress;
        this.destinationAddress = destinationAddress;
        this.status = status;
    }
}
