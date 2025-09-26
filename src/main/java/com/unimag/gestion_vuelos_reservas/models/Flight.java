package com.unimag.gestion_vuelos_reservas.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "flight_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String number;
    @Column(nullable = false, name = "departure_time")
    private OffsetDateTime departureTime;
    @Column(nullable = false,name = "arrival_time")
    private OffsetDateTime arrivalTime;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    private Airline airline;

    @ManyToOne
    @JoinColumn(name = "origin_airport_id")
    private Airport origin;

    @ManyToOne
    @JoinColumn(name = "destination_airport_id")
    private Airport destination;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @JoinTable(
            name = "flight_tags",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    // helper
    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getFlights().add(this);
    }

}
