package com.unimag.gestion_vuelos_reservas.services.mapper;

import com.unimag.gestion_vuelos_reservas.api.dto.AirportDtos;
import com.unimag.gestion_vuelos_reservas.api.dto.FlightDtos;
import com.unimag.gestion_vuelos_reservas.models.Airport;
import com.unimag.gestion_vuelos_reservas.models.Flight;

import java.util.List;
import java.util.function.Function;

public class AirportMapper {
    public Airport toEntity(AirportDtos.AirportCreateRequest request) {
        if (request == null) return null;

        return Airport.builder()
                .code(request.code().toUpperCase())
                .name(request.name().trim())
                .city(request.city().trim())
                .build();
    }

    public void updateEntity(Airport airport, AirportDtos.AirportUpdateRequest request) {
        if (airport == null || request == null) return;

        if (request.code() != null) {
            airport.setCode(request.code().toUpperCase());
        }
        if (request.name() != null) {
            airport.setName(request.name().trim());
        }
        if (request.city() != null) {
            airport.setCity(request.city().trim());
        }
    }

    public AirportDtos.AirportResponse toResponse(Airport entity) {
        if (entity == null) return null;
        return new AirportDtos.AirportResponse(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getCity()
        );
    }
}
