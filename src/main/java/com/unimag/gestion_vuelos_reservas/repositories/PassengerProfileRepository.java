package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.PassengerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerProfileRepository extends JpaRepository<PassengerProfile,Long> {
}
