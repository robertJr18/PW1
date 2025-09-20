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
    private int id;

    private String phone;
    private String CountryCode;

}
