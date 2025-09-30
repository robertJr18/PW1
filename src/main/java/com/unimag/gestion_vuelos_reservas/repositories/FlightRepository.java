package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    // 1) Obtener lista de vuelos operados por una aerolinea
    Page<Flight> findByAirline_Name(String airlineName, Pageable pageable);

    // 2) Busca vuelos por origen, destino y ventana de salida
    Page<Flight> findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(
            String originIata,
            String destinationIata,
            OffsetDateTime from,
            OffsetDateTime to,
            Pageable pageable
    );

    // 3) Filtra por origen/destino y ventana de salida,
    @EntityGraph(attributePaths = {"airline", "origin", "destination", "tags"})
    @Query("""
       SELECT f FROM Flight f
       WHERE (:o IS NULL OR f.origin.code = :o)
         AND (:d IS NULL OR f.destination.code = :d)
         AND f.departureTime BETWEEN :from AND :to
       """)
    List<Flight> searchWithAssociations(@Param("o") String origin,
                                        @Param("d") String destination,
                                        @Param("from") OffsetDateTime from,
                                        @Param("to") OffsetDateTime to);

    // 4) Devuelve los vuelos que poseen todas las tags indicadas
    @Query(value = """
       SELECT f.* FROM flights f
       JOIN flight_tags ft ON ft.flight_id = f.id
       JOIN tags t ON t.id = ft.tag_id
       WHERE t.name IN (:tags)
       GROUP BY f.id
       HAVING COUNT(DISTINCT t.name) = :requiredCount
       """, nativeQuery = true)
    List<Flight> findFlightsWithAllTags(@Param("tags") Collection<String> tags,
                                        @Param("requiredCount") long requiredCount);

    List<Flight> findByOrigin_IdAndDestination_Id(Long originId, Long destinationId);
}
