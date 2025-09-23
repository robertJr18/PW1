package com.unimag.gestion_vuelos_reservas.models;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "airports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String code;
    private String name;
    private String city;

    @OneToMany(mappedBy = "origin")
    @Builder.Default
    private List<Flight> departures = new ArrayList<>();

    @OneToMany(mappedBy = "destination")
    @Builder.Default
    private List<Flight> arrivals = new ArrayList<>();


    public void addDeparture(Flight flight) {
        departures.add(flight);
        flight.setOrigin(this);
    }

    public void addArrival(Flight flight) {
        arrivals.add(flight);
        flight.setDestination(this);
    }

}
