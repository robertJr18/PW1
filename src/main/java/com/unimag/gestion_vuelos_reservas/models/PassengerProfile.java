package com.unimag.gestion_vuelos_reservas.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "passengers_profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_profile_id")
    private long id;
    @Column(nullable = false,unique = true)
    private String phone;
    @Column(nullable = false, name = "country_code")
    private String countryCode;

}
