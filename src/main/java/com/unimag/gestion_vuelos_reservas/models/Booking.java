package com.unimag.gestion_vuelos_reservas.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private OffsetDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;
    private List<BookingItem> items;

    public void addItem(BookingItem item) {
        this.items.add(item);
    }


}
