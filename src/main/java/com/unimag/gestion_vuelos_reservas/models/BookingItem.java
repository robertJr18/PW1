package com.unimag.gestion_vuelos_reservas.models;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "booking_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;
    private Integer segmentOrder;

    //private Cabin cabin;
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    //private Flight flight;
}
