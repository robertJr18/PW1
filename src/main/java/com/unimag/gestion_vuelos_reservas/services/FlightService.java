package com.unimag.gestion_vuelos_reservas.services;

import com.unimag.gestion_vuelos_reservas.api.dto.FlightDtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

public interface FlightService {

    FlightDtos.FlightResponse create(FlightDtos.FlightCreateRequest request);

    FlightDtos.FlightResponse getById(Long id);

    List<FlightDtos.FlightResponse> getAll();

    FlightDtos.FlightResponse update(Long id, FlightDtos.FlightUpdateRequest request);

    void delete(Long id);

    ////////////////////////////////
    Page<FlightDtos.FlightResponse> findByAirlineName(String airlineName, Pageable pageable);

    Page<FlightDtos.FlightResponse> searchFlights(
            String originCode,
            String destinationCode,
            OffsetDateTime from,
            OffsetDateTime to,
            Pageable pageable
    );

    List<FlightDtos.FlightResponse> searchFlightsWithDetails(
            String originCode,
            String destinationCode,
            OffsetDateTime from,
            OffsetDateTime to
    );

    List<FlightDtos.FlightResponse> findFlightsWithAllTags(Collection<String> tagNames);
}
