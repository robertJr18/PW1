package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface AirlineRepository extends JpaRepository<Airline,Long> {

    // 1) Obtener una aerolinea por código IATA
    Optional<Airline> findByCode(String code);
}
