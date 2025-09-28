package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger,Long> {
    // 1) Buscar por email ignorando mayúsculas/minúsculas (query method)
    Optional<Passenger> findByEmailIgnoreCase(String email);

    // 2) Buscar por email (ignoring case) y precargar PassengerProfile para evitar N+1
    @Query("SELECT p FROM Passenger p LEFT JOIN FETCH p.profile WHERE LOWER(p.email) = LOWER(:email)")
    Optional<Passenger> findByEmailWithProfileIgnoreCase(@Param("email") String email);
}


