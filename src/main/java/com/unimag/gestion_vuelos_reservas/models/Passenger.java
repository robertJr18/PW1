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
    private int id;
    @Column(nullable = false, length = 120)
    private String fullName;
    private String email;
    @OneToOne(cascade = CascadeType.ALL)@JoinColumn(name = "profile_id",unique = true)
    PassengerProfile profile;


}
