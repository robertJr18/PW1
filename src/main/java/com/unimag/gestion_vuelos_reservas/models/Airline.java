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
    @Column(name = "airline_id")
    private Long id;
    @Column(nullable = false,unique = true)
    private String code;
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "airline", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Flight> flights =  new ArrayList<>();

    public void addFlight(Flight f) {
        flights.add(f);
        f.setAirline(this);
    }
}
