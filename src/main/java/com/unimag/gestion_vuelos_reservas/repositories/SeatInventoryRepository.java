package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.Cabin;
import com.unimag.gestion_vuelos_reservas.models.SeatInventory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeatInventoryRepository extends JpaRepository<SeatInventory, Long> {

    // 1) Busca el inventario de asientos de un vuelo específico en una cabina determinada
    Optional<SeatInventory> findByFlight_IdAndCabin(Long flightId, Cabin cabin);

    // 2)  Verifica si el vuelo tiene al menos la cantidad mínima de asientos disponibles en cierta cabina
    @Query("""
           SELECT CASE WHEN seatInv.availableSeats >= :min THEN TRUE ELSE FALSE END
           FROM SeatInventory seatInv
           WHERE seatInv.flight.id = :flightId
             AND seatInv.cabin = :cabin
           """)
    boolean hasAvailableSeats(@Param("flightId") Long flightId,
                              @Param("cabin") Cabin cabin,
                              @Param("min") Integer min);

    @Modifying
    @Transactional
    @Query("UPDATE SeatInventory s SET s.availableSeats = s.availableSeats - :qty " +
            "WHERE s.flight.id = :flightId AND s.cabin = :cabin AND s.availableSeats >= :qty")
    int decrementAvailableSeats(@Param("flightId") Long flightId, @Param("cabin") Cabin cabin, @Param("qty") int qty);

    @Modifying
    @Transactional
    @Query("UPDATE SeatInventory s SET s.availableSeats = s.availableSeats + :qty " +
            "WHERE s.flight.id = :flightId AND s.cabin = :cabin")
    int incrementAvailableSeats(@Param("flightId") Long flightId, @Param("cabin") Cabin cabin, @Param("qty") int qty);
}
