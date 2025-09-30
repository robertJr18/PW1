package com.unimag.gestion_vuelos_reservas.services.mapper;

import com.unimag.gestion_vuelos_reservas.api.dto.FlightDtos;
import com.unimag.gestion_vuelos_reservas.models.Airline;
import com.unimag.gestion_vuelos_reservas.models.Airport;
import com.unimag.gestion_vuelos_reservas.models.Flight;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FlightMapper {

    public static Flight toEntity(FlightDtos.FlightCreateRequest dto) {
        if (dto == null) return null;
        Flight entity = new Flight();
        entity.setNumber(dto.number());
        entity.setDepartureTime(dto.departureTime());
        entity.setArrivalTime(dto.arrivalTime());
        // Resto de atributos en services
        return entity;
    }

    public static void toUpdateEntity(FlightDtos.FlightUpdateRequest dto, Flight entity) {
        if (dto == null || entity == null) return;
        entity.setNumber(dto.number());
        entity.setDepartureTime(dto.departureTime());
        entity.setArrivalTime(dto.arrivalTime());
        // Resto de atributos en services
    }

    public static FlightDtos.FlightResponse toResponse(Flight entity) {
        if (entity == null) return null;

        FlightDtos.AirlineRef airlineRef = null;
        if (entity.getAirline() != null) {
            Airline a = entity.getAirline();
            airlineRef = new FlightDtos.AirlineRef(a.getId(), a.getCode(), a.getName());
        }

        FlightDtos.AirportRef originRef = null;
        if (entity.getOrigin() != null) {
            Airport o = entity.getOrigin();
            originRef = new FlightDtos.AirportRef(o.getId(), o.getCode(), o.getCity());
        }

        FlightDtos.AirportRef destinationRef = null;
        if (entity.getDestination() != null) {
            Airport d = entity.getDestination();
            destinationRef = new FlightDtos.AirportRef(d.getId(), d.getCode(), d.getCity());
        }

        Set<FlightDtos.TagRef> tagRefs = Set.of();
        if (entity.getTags() != null) {
            tagRefs = entity.getTags().stream()
                    .filter(Objects::nonNull)
                    .map(t -> new FlightDtos.TagRef(t.getId(), t.getName()))
                    .collect(Collectors.toSet());
        }

        return new FlightDtos.FlightResponse(
                entity.getId(),
                entity.getNumber(),
                entity.getDepartureTime(),
                entity.getArrivalTime(),
                airlineRef,
                originRef,
                destinationRef,
                tagRefs
        );
    }
}
