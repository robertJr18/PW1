package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface AirportRepository extends JpaRepository<Airport, Long> {

    // 1) Obtener aeropuerto por código IATA
    Optional<Airport> findByCode(String code);
}
