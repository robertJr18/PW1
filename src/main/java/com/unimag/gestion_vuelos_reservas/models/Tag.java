package com.unimag.gestion_vuelos_reservas.models;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private Set<Flight> flights = new HashSet<>();

    public void addFlight(Flight flight) {
        flights.add(flight);
        flight.getTags().add(this);
    }
}
