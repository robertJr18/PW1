package com.unimag.gestion_vuelos_reservas.services.mapper;

import com.unimag.gestion_vuelos_reservas.api.dto.AirlineDtos;
import com.unimag.gestion_vuelos_reservas.api.dto.FlightDtos;
import com.unimag.gestion_vuelos_reservas.models.Airline;
import com.unimag.gestion_vuelos_reservas.models.Flight;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class AirlineMapper {
    public Airline toEntity(AirlineDtos.AirlineCreateRequest dto) {
        if (dto == null) return null;
        Airline entity = new Airline();
        entity.setCode(dto.code());
        entity.setName(dto.name());
        return entity;
    }

    public Airline toUpdateEntity(AirlineDtos.AirlineUpdateRequest dto) {
        if (dto == null) return null;
        Airline entity = new Airline();
        entity.setCode(dto.code());
        entity.setName(dto.name());
        return entity;
    }

    public AirlineDtos.AirlineResponse toResponse(
            Airline entity,
            boolean includeFlights,
            Function<Flight, FlightDtos.FlightResponse> flightMapper
    ) {
        if (entity == null) return null;
        List<FlightDtos.FlightResponse> flightsDto = List.of();
        if (includeFlights && entity.getFlights() != null && flightMapper != null) {
            flightsDto = entity.getFlights().stream()
                    .filter(Objects::nonNull)
                    .map(flightMapper)
                    .toList();
        }
        return new AirlineDtos.AirlineResponse(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                flightsDto
        );
    }
}
