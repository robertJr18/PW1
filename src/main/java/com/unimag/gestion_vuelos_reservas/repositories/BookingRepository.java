package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.Booking;
import jakarta.persistence.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    // 1) Paginar reservas de un pasajero (match por email, ignoring case) ordenadas por createdAt desc
    Page<Booking> findByPassengerEmailIgnoreCaseOrderByCreatedAtDesc(String email, Pageable pageable);

    // 2) Trae una reserva por id precargando items, items.flight y passenger (evita múltiples queries)
    @EntityGraph(attributePaths = {"items", "items.flight", "passenger"})
    @Query("SELECT b FROM Booking b WHERE b.id = :id")
    Optional<Booking> searchWithAllDetails(@Param("id")Long id);

}
