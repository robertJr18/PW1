package com.unimag.gestion_vuelos_reservas.services.mapper;

import com.unimag.gestion_vuelos_reservas.api.dto.AirportDtos;
import com.unimag.gestion_vuelos_reservas.api.dto.FlightDtos;
import com.unimag.gestion_vuelos_reservas.models.Airport;
import com.unimag.gestion_vuelos_reservas.models.Flight;

import java.util.List;
import java.util.function.Function;

public class AirportMapper {
    public Airport (AirportDtos.AirportCreateRequest dto) {
        if (dto == null) return null;
        Airport entity = new Airport();
        entity.setCode(dto.code());
        entity.setName(dto.name());
        entity.setCity(dto.city());
        return entity;
    }

    public void toUpdateEntity(AirportDtos.AirportUpdateRequest dto, Airport entity) {
        if (dto == null || entity == null) return;
        entity.setCode(dto.code());
        entity.setName(dto.name());
        entity.setCity(dto.city());
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
