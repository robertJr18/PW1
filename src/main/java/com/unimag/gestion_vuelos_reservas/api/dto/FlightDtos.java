package com.unimag.gestion_vuelos_reservas.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;


public class FlightDtos {
    public record AirlineRef(Long id, String code, String name) implements Serializable {}
    public record AirportRef(Long id, String code, String city) implements Serializable {}
    public record TagRef(Long id, String name) implements Serializable {}


    public record FlightCreateRequest(@NotBlank @Max(7) String number, @NotNull OffsetDateTime departureTime, @NotNull OffsetDateTime arrivalTime,
                                      @NotNull Long airlineId, @NotNull Long originId, @NotNull Long destinationId, @NotNull Set<Long> tagIds)implements Serializable {}

    public record FlightUpdateRequest(String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime,
                                      Long airlineId,Long originId, Long destinationId, Set<Long> tagIds)implements Serializable {}

    public record FlightResponse(Long id, String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime,
            AirlineRef airline, AirportRef origin, AirportRef destination, Set<TagRef> tags) implements Serializable {}
}
