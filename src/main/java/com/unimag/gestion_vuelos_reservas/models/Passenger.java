package com.unimag.gestion_vuelos_reservas.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "passengers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_id")
    private long id;
    @Column(nullable = false, length = 120)
    private String fullName;
    @Column(nullable = false, unique = true)
    private String email;
    @OneToOne
    @JoinColumn(name = "profile_id",unique = true)
    PassengerProfile profile;


}
