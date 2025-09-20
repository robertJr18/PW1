package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.BookingItem;
import com.unimag.gestion_vuelos_reservas.models.Cabin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;


public interface BookingItemRepository  extends JpaRepository<BookingItem,Long> {

    // 1) Lista los segmentos de una reserva ordenados por segmentOrder asc
    List<BookingItem> findByBookingIdOrderBySegmentOrderAsc(Long bookingId);

    // 2) Calcula el total de la reserva sumando precios de sus ítems (SUM + COALESCE)
    @Query("SELECT COALESCE(SUM(bi.price), 0) FROM BookingItem bi WHERE bi.booking.id = :bookingId")
    BigDecimal sumPriceByBookingId(@Param("bookingId") Long bookingId);

    // 3) Cuenta cuántos asientos han sido vendidos/reservados para un vuelo y cabina
    @Query("SELECT COUNT(bi) FROM BookingItem bi WHERE bi.flight.id = :flightId AND bi.cabin = :cabin")
    long countByFlightIdAndCabin(@Param("flightId") Long flightId, @Param("cabin") Cabin cabin);
}
