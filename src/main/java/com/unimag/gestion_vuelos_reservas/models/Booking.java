package com.unimag.gestion_vuelos_reservas.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "booking_id")
    private Long id;
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @OneToMany(mappedBy = "booking",fetch = FetchType.LAZY)
    private List<BookingItem> items = new ArrayList<>();

    public void addItem(BookingItem item) {
        this.items.add(item);
        item.setBooking(this);
    }


}
