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
    private Long id;
    //private Cabin cabin;
    private Integer totalSeats;
    private Integer availableSeats;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;
}
