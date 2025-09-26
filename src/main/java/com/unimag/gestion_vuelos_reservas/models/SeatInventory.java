package com.unimag.gestion_vuelos_reservas.models;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seatsInventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seat_inventory_id")
    private Long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Cabin cabin;
    @Column(nullable = false,name = "total_seats")
    private Integer totalSeats;
    @Column(nullable = false,name = "available_seats")
    private Integer availableSeats;

    @ManyToOne
    @JoinColumn(name = "flight_id",nullable = false)
    private Flight flight;
}
