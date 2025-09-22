package com.unimag.gestion_vuelos_reservas.models;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "airlines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String code;
    private String name;

    @OneToMany(mappedBy = "flights")
    @Builder.Default
    private List<Flight> flights =  new ArrayList<>();

    public void addFlight(Flight f) {
        flights.add(f);
        f.setAirline(this);
    }
}
