package com.unimag.gestion_vuelos_reservas.api.dto;

import com.unimag.gestion_vuelos_reservas.models.Cabin;

import java.io.Serializable;

public class SeatInventoryDtos {
    public record SeatInventoryCreateRequest(Cabin cabin, Integer totalSeats, Integer availableSeats,Long flightId) implements Serializable {}
    public record SeatInventoryUpdateRequest(Cabin cabin, Integer totalSeats, Integer availableSeats, Long flightId) implements Serializable {}
    public record SeatInventoryResponse(Long id, Cabin cabin, Integer totalSeats, Integer availableSeats, FlightDtos.FlightResponse flight) implements Serializable {}
}
